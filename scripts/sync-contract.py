#!/usr/bin/env python3
"""把平台仓库的 OpenAPI 契约与签名向量同步到本仓。

本仓是独立仓库：客户只拿到这个仓，读不到平台源码，所以契约和签名向量必须各存一份。
但「各存一份」天生会漂移，而漂移的表现是「签名明明按文档写了却一直 401」。
所以每次同步都把来源 commit 与哈希写进 contract/SOURCE.md，
verify-parity.py 拿它去核对，而不是靠人记得重跑。

只有平台侧维护者需要跑这个脚本；客户拿到的已经是同步好的副本。

    python3 scripts/sync-contract.py
    python3 scripts/sync-contract.py --platform ~/code/uvp/UVP-GB28181

退出码：0 成功 / 2 找不到平台仓库
"""

from __future__ import annotations

import argparse
import datetime
import hashlib
import os
import shutil
import subprocess
import sys

REPO_ROOT = os.path.dirname(os.path.dirname(os.path.abspath(__file__)))

SPEC_TARGET = os.path.join(REPO_ROOT, "contract", "openapi-v1.yaml")
FIXTURE_TARGET = os.path.join(REPO_ROOT, "fixtures", "signature-v1.json")
NOTE = os.path.join(REPO_ROOT, "contract", "SOURCE.md")

SPEC_RELATIVE = os.path.join("api", "openapi-v1.yaml")
FIXTURE_RELATIVE = os.path.join(
    "server", "app", "openapi", "auth", "testdata", "signature-v1.json")


def find_platform_root(explicit: str | None) -> str | None:
    """按显式参数、环境变量、同级目录的顺序找平台仓库。"""
    candidates = []
    if explicit:
        candidates.append(explicit)
    if os.environ.get("UVP_PLATFORM_ROOT"):
        candidates.append(os.environ["UVP_PLATFORM_ROOT"])
    parent = os.path.dirname(REPO_ROOT)
    candidates.append(os.path.join(parent, "UVP-GB28181"))
    candidates.append(os.path.join(parent, "uvp-gb28181"))

    for root in candidates:
        if root and os.path.isfile(os.path.join(root, SPEC_RELATIVE)):
            return os.path.abspath(root)
    return None


def digest(path: str) -> str:
    with open(path, "rb") as handle:
        return hashlib.sha256(handle.read()).hexdigest()


def platform_commit(root: str) -> str:
    try:
        return subprocess.run(["git", "-C", root, "rev-parse", "--short", "HEAD"],
                              capture_output=True, text=True, check=True).stdout.strip()
    except (subprocess.CalledProcessError, FileNotFoundError):
        return "unknown"


def main() -> int:
    parser = argparse.ArgumentParser(description=__doc__)
    parser.add_argument("--platform", default=None, help="平台仓库路径")
    args = parser.parse_args()

    root = find_platform_root(args.platform)
    if root is None:
        print("✗ 找不到平台仓库（需要它提供 api/openapi-v1.yaml）\n"
              "  用 --platform 指定，或设置 UVP_PLATFORM_ROOT。\n"
              "  如果你只是使用本仓的客户，不需要跑这个脚本。", file=sys.stderr)
        return 2

    spec_source = os.path.join(root, SPEC_RELATIVE)
    fixture_source = os.path.join(root, FIXTURE_RELATIVE)
    if not os.path.isfile(fixture_source):
        print("✗ 平台仓库里找不到签名向量: %s" % fixture_source, file=sys.stderr)
        return 2

    os.makedirs(os.path.dirname(SPEC_TARGET), exist_ok=True)
    os.makedirs(os.path.dirname(FIXTURE_TARGET), exist_ok=True)
    shutil.copyfile(spec_source, SPEC_TARGET)
    shutil.copyfile(fixture_source, FIXTURE_TARGET)

    spec_digest = digest(SPEC_TARGET)
    fixture_digest = digest(FIXTURE_TARGET)
    commit = platform_commit(root)
    now = datetime.datetime.now().strftime("%Y-%m-%d %H:%M:%S")
    # 记相对路径而不是绝对路径：绝对路径会写死某台机器的目录结构，
    # 换个人同步就产生无意义 diff，也会把开发机的目录名带进交付物。
    relative_root = os.path.relpath(root, REPO_ROOT)

    with open(NOTE, "w", encoding="utf-8") as handle:
        handle.write(
            "# 契约与签名向量的来源\n\n"
            "这两个文件不是手写的，是从平台仓库同步过来的副本。改它们不算改代码，算改契约。\n\n"
            "| 文件 | 来源（平台仓库内） | SHA-256 |\n"
            "|---|---|---|\n"
            "| `contract/openapi-v1.yaml` | `api/openapi-v1.yaml` | `%s` |\n"
            "| `fixtures/signature-v1.json` | `server/app/openapi/auth/testdata/signature-v1.json` | `%s` |\n\n"
            "| 项 | 值 |\n|---|---|\n"
            "| 同步时平台 commit | `%s` |\n"
            "| 同步时间 | %s |\n"
            "| 平台仓库路径 | `%s` |\n\n"
            "重新同步：`python3 scripts/sync-contract.py`\n\n"
            "同步完必须跑 `python3 scripts/verify-parity.py`。契约变了而三份实现没跟上时，\n"
            "这个脚本会红 —— 那正是它存在的意义。\n"
            % (spec_digest, fixture_digest, commit, now, relative_root)
        )

    print("✓ 契约  -> contract/openapi-v1.yaml  sha256=%s..." % spec_digest[:16])
    print("✓ 向量  -> fixtures/signature-v1.json  sha256=%s..." % fixture_digest[:16])
    print("  来源平台 commit %s（%s）" % (commit, root))
    print("\n下一步：python3 scripts/verify-parity.py")
    return 0


if __name__ == "__main__":
    sys.exit(main())
