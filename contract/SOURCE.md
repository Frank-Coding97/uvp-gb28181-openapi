# 契约与签名向量的来源

这两个文件不是手写的，是从平台仓库同步过来的副本。改它们不算改代码，算改契约。

| 文件 | 来源（平台仓库内） | SHA-256 |
|---|---|---|
| `contract/openapi-v1.yaml` | `api/openapi-v1.yaml` | `531f7db6d3db8ec622c53e1094d73af137d0a9575d2ec68ff297f0edf598513a` |
| `fixtures/signature-v1.json` | `server/app/openapi/auth/testdata/signature-v1.json` | `6ba7f337166189df97a4a2ed8188bc6b3f44380ebbb93c453e4d7546d5a1fc05` |

| 项 | 值 |
|---|---|
| 同步时平台 commit | `3dabaad4` |
| 同步时间 | 2026-11-09 23:20:17 |
| 平台仓库路径 | `../UVP-GB28181` |

重新同步：`python3 scripts/sync-contract.py`

同步完必须跑 `python3 scripts/verify-parity.py`。契约变了而三份实现没跟上时，
这个脚本会红 —— 那正是它存在的意义。
