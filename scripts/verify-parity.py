#!/usr/bin/env python3
"""校验 SDK 里所有语言的签名实现都落在同一份向量上。

这个脚本存在的理由：签名错的表现永远是"401"，而 401 与 AK/SK 填错长得一模一样。
真正容易出事的不是某一门语言写错，而是**只有一门语言被改对了**：
例如有人调了 Java 的 canonical，Python 和 Go 还停在旧逻辑，客户换语言接入就开始报 401。

顺带做地址体检：这个仓库要交给客户，真机地址和内网 IP 不能跟着源码一起发出去。

还会重跑一次 HTTP 集合生成器并逐字节比对：契约改了却忘了重新生成，
集合就会拿着旧路径去调，客户会以为平台有问题。

如果旁边就放着平台仓库，会顺手核对 contract/ 与 fixtures/ 两份副本有没有过期；
客户侧没有平台仓库，这几条自动跳过。

    python3 scripts/verify-parity.py                # 全集（不含 Java）
    python3 scripts/verify-parity.py --with-java    # 再加 Java（需要 mvn）
    python3 scripts/verify-parity.py --fixture-only # 只查副本有没有漂移（CI 最快档）

退出码：0 全过 / 1 有不一致 / 2 环境缺失
"""

from __future__ import annotations

import argparse
import hashlib
import os
import shutil
import subprocess
import sys
import tempfile

REPO_ROOT = os.path.dirname(os.path.dirname(os.path.abspath(__file__)))
FIXTURE = os.path.join(REPO_ROOT, "fixtures", "signature-v1.json")
HTTP_DIR = os.path.join(REPO_ROOT, "http")
CONTRACT = os.path.join(REPO_ROOT, "contract", "openapi-v1.yaml")

PLATFORM_SPEC = os.path.join("api", "openapi-v1.yaml")
PLATFORM_FIXTURE = os.path.join(
    "server", "app", "openapi", "auth", "testdata", "signature-v1.json")

HTTP_ARTIFACTS = [
    "uvp-openapi.postman_collection.json",
    "uvp-openapi.postman_environment.json",
    "uvp-openapi.apifox.json",
]


def run(label: str, command: list[str], cwd: str | None = None) -> bool:
    print("\n▶ %s" % label)
    result = subprocess.run(command, cwd=cwd, capture_output=True, text=True)
    output = (result.stdout + result.stderr).rstrip()
    if output:
        print(output)
    if result.returncode != 0:
        print("✗ %s 失败（exit=%d）" % (label, result.returncode), file=sys.stderr)
        return False
    return True


def find_platform_root() -> str | None:
    """本仓旁边是否放着平台仓库。客户拿到的独立仓没有，这些检查自动跳过。"""
    explicit = os.environ.get("UVP_PLATFORM_ROOT")
    candidates = []
    if explicit:
        candidates.append(explicit)
    parent = os.path.dirname(REPO_ROOT)
    candidates += [os.path.join(parent, "UVP-GB28181"), os.path.join(parent, "uvp-gb28181")]
    for root in candidates:
        if root and os.path.isfile(os.path.join(root, PLATFORM_SPEC)):
            return os.path.abspath(root)
    return None


def check_copies_against_platform() -> bool:
    """contract/ 与 fixtures/ 都是副本，副本必须与平台原件字节一致。

    这是拆仓带来的唯一新增风险：以前改契约和改 SDK 在同一个仓，编译器/测试会互相提醒；
    现在隔着两个仓，谁也不会替谁报错。所以只要能看到平台仓库，就逐个字节核对。
    """
    if not os.path.isfile(FIXTURE):
        print("✗ 缺少 %s" % os.path.relpath(FIXTURE, REPO_ROOT), file=sys.stderr)
        return False
    if not os.path.isfile(CONTRACT):
        print("✗ 缺少 %s（客户拿不到契约就无法自证签名）"
              % os.path.relpath(CONTRACT, REPO_ROOT), file=sys.stderr)
        return False

    platform = find_platform_root()
    if platform is None:
        print("· 旁边没有平台仓库，跳过副本对账（独立仓正常情况）")
        return True

    def digest(path: str) -> str:
        with open(path, "rb") as handle:
            return hashlib.sha256(handle.read()).hexdigest()

    pairs = [
        (CONTRACT, os.path.join(platform, PLATFORM_SPEC), "契约"),
        (FIXTURE, os.path.join(platform, PLATFORM_FIXTURE), "签名向量"),
    ]
    ok = True
    for local, source, label in pairs:
        if not os.path.isfile(source):
            print("· 平台仓库缺少 %s，跳过%s对账" % (label, label))
            continue
        local_digest = digest(local)
        source_digest = digest(source)
        if local_digest != source_digest:
            print("✗ %s 副本已过期\n  副本: %s\n  原件: %s\n"
                  "  修复: python3 scripts/sync-contract.py"
                  % (label, local_digest, source_digest), file=sys.stderr)
            ok = False
        else:
            print("✓ %s副本与平台一致 sha256=%s..." % (label, local_digest[:16]))
    return ok


def check_http_collection_fresh(python: str) -> bool:
    """重跑生成器到临时目录，与仓库里的产物逐字节比对。

    只比「有没有被重新生成过」是不够的 —— 契约改一个路径，集合不会报错，
    只会一直拿旧地址去调。字节比对能立刻发现。
    """
    generator = os.path.join(REPO_ROOT, "scripts", "gen-http-collection.py")
    with tempfile.TemporaryDirectory() as scratch:
        result = subprocess.run([python, generator, "--out", scratch],
                                capture_output=True, text=True)
        if result.returncode != 0:
            print("✗ 生成器执行失败\n%s" % (result.stdout + result.stderr).rstrip(),
                  file=sys.stderr)
            return False

        def digest(path: str) -> str:
            with open(path, "rb") as handle:
                return hashlib.sha256(handle.read()).hexdigest()

        stale = []
        for name in HTTP_ARTIFACTS:
            fresh = os.path.join(scratch, name)
            shipped = os.path.join(HTTP_DIR, name)
            if not os.path.isfile(shipped):
                stale.append("%s（缺文件）" % name)
                continue
            if digest(fresh) != digest(shipped):
                stale.append("%s（内容过期）" % name)

        if stale:
            print("✗ HTTP 集合与契约不同步：%s\n"
                  "  修复: python3 scripts/gen-http-collection.py"
                  % "、".join(stale), file=sys.stderr)
            return False
    print("✓ HTTP 集合与契约同步（%d 个产物）" % len(HTTP_ARTIFACTS))
    return True


def main() -> int:
    parser = argparse.ArgumentParser(description=__doc__)
    parser.add_argument("--fixture-only", action="store_true", help="只对账夹具，不跑语言自测")
    parser.add_argument("--with-java", action="store_true", help="额外跑 Java 单测（需要 mvn）")
    args = parser.parse_args()

    ok = check_copies_against_platform()
    if args.fixture_only:
        return 0 if ok else 1

    python = shutil.which("python3") or sys.executable
    ok = run("地址体检（对外交付不许出现真实平台/内网 IP）",
             [python, os.path.join(REPO_ROOT, "scripts", "check-no-endpoints.py")]) and ok
    ok = check_http_collection_fresh(python) and ok

    node = shutil.which("node")
    if node is None:
        print("\n! 未找到 node，跳过 Postman/Apifox 脚本验签（CI 里必须跑）", file=sys.stderr)
        ok = False
    else:
        ok = run("Postman/Apifox 脚本验签",
                 [node, os.path.join(REPO_ROOT, "scripts", "verify-http-collection.js")]) and ok

    ok = run("Python 自测", [python, os.path.join(REPO_ROOT, "python", "selftest.py")]) and ok

    go = shutil.which("go")
    if go is None:
        print("\n! 未找到 go，跳过 Go 自测（CI 里必须跑）", file=sys.stderr)
        ok = False
    else:
        ok = run("Go 自测", [go, "run", "./cmd/demo", "--self-test"],
                 cwd=os.path.join(REPO_ROOT, "go")) and ok

    if args.with_java:
        mvn = shutil.which("mvn")
        if mvn is None:
            print("\n! 未找到 mvn，跳过 Java 单测（CI 里必须跑）", file=sys.stderr)
            ok = False
        else:
            ok = run("Java 单测（HmacSignerTest）",
                     [mvn, "-B", "-q", "-pl", "uvp-openapi-sdk", "test",
                      "-Dtest=HmacSignerTest", "-DfailIfNoTests=false"],
                     cwd=os.path.join(REPO_ROOT, "java")) and ok

    print("\n%s" % ("✓ 全部一致" if ok else "✗ 存在不一致，见上面输出"))
    return 0 if ok else 1


if __name__ == "__main__":
    sys.exit(main())
