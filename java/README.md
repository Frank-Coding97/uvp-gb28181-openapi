# UVP OpenAPI Java SDK

覆盖平台当前 106 个 `/openapi/v1/*` 接口，按业务域分为 8 组。所有接口使用短路径和明确动作：GET 参数放 query，POST 参数全部放 JSON body；设备、通道和会话标识均作为入参。

SDK 自动处理 AK/SK 签名、幂等键、GET 重试和错误映射。模型包含设备与通道详情、异步操作状态、媒体地址、云录像和报警详情，嵌套对象及动态 map 保留具体类型，nullable 字段保留 `null`。

基础 SDK 编译目标为 Java 8；Spring Boot 3 starter 和 demo 需要 Java 17。

## Spring Boot 接入

```xml
<dependency>
  <groupId>com.uvp</groupId>
  <artifactId>uvp-openapi-spring-boot-starter</artifactId>
  <version>1.0.0-SNAPSHOT</version>
</dependency>
```

```yaml
uvp:
  openapi:
    base-url: ${UVP_BASE_URL:http://localhost:8280}
    access-key: ${UVP_ACCESS_KEY:}
    secret-key: ${UVP_SECRET_KEY:}
    audience: ${UVP_AUDIENCE:} # 必须等于平台 openapi.audience
```

```java
import com.uvp.openapi.client.UvpClient;
import com.uvp.openapi.model.*;

// 注入 starter 创建的 UvpClient；同一平台和 AK 复用一个实例。
Device device = uvp.devices().getDevice(deviceId).getData();
LivePlayback live = uvp.live().startLivePlayback(
        new ActionStartLivePlaybackRequest().deviceId(deviceId).channelId(channelId)).getData();
String hls = live.getUrls().getHls();
uvp.ptz().callPTZPreset(new ActionCallPTZPresetRequest()
        .deviceId(deviceId).channelId(channelId).presetId("1"));
```

未使用 Spring 时自行创建并关闭客户端：

```java
try (UvpClient uvp = new UvpClient(UvpClientConfig.of(baseUrl, accessKey, secretKey, audience))) {
    DeviceResponse response = uvp.devices().getDevice(deviceId);
}
```

可选筛选使用完整重载；简短重载仅保留必填参数。例如 `listDevices()` 查询默认分页，`listChannels(deviceId)` 查询设备的通道。布尔筛选的 `false` 和数字筛选的 `0` 会按原值传输，`null` 表示省略。

## API 分组

| 方法 | 覆盖范围 |
|---|---|
| `uvp.devices()` | 设备、通道、统计、编辑、状态、目录刷新、抓拍和图像库 |
| `uvp.live()` | 实时点播启停、会话详情和列表、关键帧请求 |
| `uvp.playback()` | 设备录像检索、回放、下载、云录像 |
| `uvp.ptz()` | 云台、预置位、巡航、精确定位、拉框缩放 |
| `uvp.talk()` | 对讲、广播及媒体协商 |
| `uvp.alarms()` | 报警、事件、订阅、位置 |
| `uvp.configs()` | 设备配置、视频参数、固件升级 |
| `uvp.control()` | 录像启停、布撤防、重启、存储卡、维护任务 |

## 录像分页

首查填写时间窗口，续页只传设备、通道、cursor 和可选 pageSize；不要把首查条件和 cursor 混在一起。

```java
DeviceRecordQuery200Response first = uvp.playback().deviceRecordQuery(
        new ActionDeviceRecordQueryRequest().deviceId(deviceId).channelId(channelId)
                .startTime(start).endTime(end).pageSize(50));
DeviceRecordQuery200Response next = uvp.playback().deviceRecordQuery(
        new ActionDeviceRecordQueryRequest().deviceId(deviceId).channelId(channelId)
                .cursor(first.getData().getNextCursor()).pageSize(50));
```

时间参数采用带时区的 RFC 3339；设备录像检索时间窗口最多 24 小时。SDK 传递平台数据，播放、对讲和下载媒体由调用方处理。

## 错误、幂等和重试

失败抛出 `UvpApiException`，可读取 `getKind()`、`getHttpStatus()`、`getCode()` 和 `getRequestId()`。记录 requestId 可与平台日志对应。设备返回 SIP 200 只表示协议应答，控制命令的设备执行结果以操作状态接口为准。

要求幂等的写接口自动生成 `Idempotency-Key`。结果未知时，调用方重试必须复用同一个键：

```java
String key = UvpIdempotency.newKey();
ActionChannelRecordingStartRequest body = new ActionChannelRecordingStartRequest()
        .deviceId(deviceId).channelId(channelId);
UvpIdempotency.run(key, () -> uvp.control().channelRecordingStart(body));
// 发生超时且决定重试时，仍使用 key 和同一份 body。
```

GET 遇到 429、5xx 或网络错误按 `max-retries` 自动重试，每次使用新 nonce 重新签名。POST 不自动重试。点播等慢接口使用 `slow-read-timeout`，普通读取使用 `read-timeout`。

接入地址必须能路由到后端 `/openapi/v1/`；根据实际部署设置 `base-url`。HTTPS 使用可信证书，`audience` 必须与平台配置一致，机器时钟应同步。

## 构建、测试和生成

```bash
cd java
mvn test
mvn package
```

测试包含共用签名向量、请求签名与错误映射，以及读取权威 YAML 后逐条调用全部 106 个方法的 MockWebServer 验证：路径、query/body、幂等头、每层响应字段、nullable 和 typed map。starter 与 demo 也纳入 reactor 测试。

契约位于 `contract/openapi-v1.yaml`。生成环境需要 Python 3 + PyYAML；模型生成另需 JDK 17（首次下载固定版本 OpenAPI Generator，后续复用 `java/.cache/`）。在仓库根执行：

```bash
python3 java/codegen/import_models.py
python3 java/codegen/generate_apis.py
python3 java/codegen/generate_apis.py --check
python3 -m unittest discover -s java/codegen -p 'test_*.py'
```

生成物随 SDK 发布，使用者不需要生成工具。`--check` 只校验，不修改文件。更新契约后重新生成模型与 API，并运行 Maven 测试。

启动 demo：

```bash
export UVP_BASE_URL=http://localhost:8280
export UVP_ACCESS_KEY=uvp_xxx
export UVP_SECRET_KEY=xxx
export UVP_AUDIENCE=uvp-xxx
java -jar java/uvp-openapi-demo/target/uvp-openapi-demo-1.0.0-SNAPSHOT.jar
curl 'http://localhost:8088/demo/devices'
```

`RealPlatformIT` 保留真实平台联调入口，默认不执行。仅在准备好测试设备与对应权限后显式运行，它会发起点播、云台停止、订阅、录像、对讲及下载操作：

```bash
UVP_IT_BASE_URL=http://localhost:8280 UVP_IT_AK=uvp_xxx UVP_IT_SK=xxx UVP_IT_AUDIENCE=uvp-xxx \
  mvn -B -f java/pom.xml -pl uvp-openapi-sdk test -Dtest=RealPlatformIT
```
