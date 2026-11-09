// GENERATED FILE - DO NOT EDIT.
// Source: contract/openapi-v1.yaml via java/codegen/generate_apis.py
package com.uvp.openapi.api;

import com.uvp.openapi.internal.ApiInvoker;
import com.uvp.openapi.internal.Call;
import com.uvp.openapi.model.*;

/**
 * Record search, playback sessions and download sessions.
 *
 * <p>Signing, retries and error mapping happen inside {@link ApiInvoker}; no method here takes a
 * signature argument.</p>
 */
public final class PlaybackApi {

    private final ApiInvoker invoker;

    public PlaybackApi(ApiInvoker invoker) {
        this.invoker = invoker;
    }

    /**
     * 读取录像检索参数
     *
     * <p>{@code GET /openapi/v1/record/options}</p>
     *
     * <p>Required scope: {@code device:record-query:options}</p>
     */
    public DeviceRecordQueryOptions200Response deviceRecordQueryOptions(String deviceId, String channelId) {
        return invoker.execute(Call.get("/openapi/v1/record/options")
            .query("deviceId", deviceId)
            .query("channelId", channelId)
            .as(DeviceRecordQueryOptions200Response.class));
    }

    /**
     * 检索设备录像
     *
     * <p>{@code POST /openapi/v1/record/search}</p>
     *
     * <p>Required scope: {@code device:record-query}</p>
     */
    public DeviceRecordQuery200Response deviceRecordQuery(ActionDeviceRecordQueryRequest body) {
        return invoker.execute(Call.post("/openapi/v1/record/search")
            .body(body)
            .idempotency(Call.Idempotency.OPTIONAL)
            .slow()
            .as(DeviceRecordQuery200Response.class));
    }

    /**
     * 创建设备录像回放
     *
     * <p>{@code POST /openapi/v1/playback/start}</p>
     *
     * <p>Required scope: {@code device:playback-create}</p>
     * <p>The platform requires an {@code Idempotency-Key} for this operation. One is
     * generated automatically; pin it with {@link com.uvp.openapi.client.UvpIdempotency}
     * when you retry after an unknown outcome.</p>
     */
    public DevicePlaybackCreate200Response devicePlaybackCreate(ActionDevicePlaybackCreateRequest body) {
        return invoker.execute(Call.post("/openapi/v1/playback/start")
            .body(body)
            .idempotency(Call.Idempotency.REQUIRED)
            .slow()
            .as(DevicePlaybackCreate200Response.class));
    }

    /**
     * 读取设备回放会话
     *
     * <p>{@code GET /openapi/v1/playback/info}</p>
     *
     * <p>Required scope: {@code device:playback-read}</p>
     */
    public DevicePlaybackRead200Response devicePlaybackRead(String deviceId, String channelId, String sessionId) {
        return invoker.execute(Call.get("/openapi/v1/playback/info")
            .query("deviceId", deviceId)
            .query("channelId", channelId)
            .query("sessionId", sessionId)
            .as(DevicePlaybackRead200Response.class));
    }

    /**
     * 控制设备录像回放
     *
     * <p>{@code POST /openapi/v1/playback/control}</p>
     *
     * <p>Required scope: {@code device:playback-action}</p>
     * <p>The platform requires an {@code Idempotency-Key} for this operation. One is
     * generated automatically; pin it with {@link com.uvp.openapi.client.UvpIdempotency}
     * when you retry after an unknown outcome.</p>
     */
    public DevicePlaybackAction200Response devicePlaybackAction(ActionDevicePlaybackActionRequest body) {
        return invoker.execute(Call.post("/openapi/v1/playback/control")
            .body(body)
            .idempotency(Call.Idempotency.REQUIRED)
            .slow()
            .as(DevicePlaybackAction200Response.class));
    }

    /**
     * 结束设备录像回放
     *
     * <p>{@code POST /openapi/v1/playback/stop}</p>
     *
     * <p>Required scope: {@code device:playback-stop}</p>
     * <p>The platform requires an {@code Idempotency-Key} for this operation. One is
     * generated automatically; pin it with {@link com.uvp.openapi.client.UvpIdempotency}
     * when you retry after an unknown outcome.</p>
     */
    public OpenAPIDevicePlaybackStopResponse devicePlaybackStop(ActionDevicePlaybackStopRequest body) {
        return invoker.execute(Call.post("/openapi/v1/playback/stop")
            .body(body)
            .idempotency(Call.Idempotency.REQUIRED)
            .slow()
            .as(OpenAPIDevicePlaybackStopResponse.class));
    }

    /**
     * 创建设备录像下载
     *
     * <p>{@code POST /openapi/v1/download/start}</p>
     *
     * <p>Required scope: {@code device:download-create}</p>
     * <p>The platform requires an {@code Idempotency-Key} for this operation. One is
     * generated automatically; pin it with {@link com.uvp.openapi.client.UvpIdempotency}
     * when you retry after an unknown outcome.</p>
     */
    public DeviceDownloadCreate200Response deviceDownloadCreate(ActionDeviceDownloadCreateRequest body) {
        return invoker.execute(Call.post("/openapi/v1/download/start")
            .body(body)
            .idempotency(Call.Idempotency.REQUIRED)
            .slow()
            .as(DeviceDownloadCreate200Response.class));
    }

    /**
     * 读取设备录像下载
     *
     * <p>{@code GET /openapi/v1/download/info}</p>
     *
     * <p>Required scope: {@code device:download-read}</p>
     */
    public DeviceDownloadRead200Response deviceDownloadRead(String deviceId, String channelId, String sessionId) {
        return invoker.execute(Call.get("/openapi/v1/download/info")
            .query("deviceId", deviceId)
            .query("channelId", channelId)
            .query("sessionId", sessionId)
            .as(DeviceDownloadRead200Response.class));
    }

    /**
     * 取消设备录像下载
     *
     * <p>{@code POST /openapi/v1/download/stop}</p>
     *
     * <p>Required scope: {@code device:download-cancel}</p>
     * <p>The platform requires an {@code Idempotency-Key} for this operation. One is
     * generated automatically; pin it with {@link com.uvp.openapi.client.UvpIdempotency}
     * when you retry after an unknown outcome.</p>
     */
    public DeviceDownloadCancel200Response deviceDownloadCancel(ActionDeviceDownloadCancelRequest body) {
        return invoker.execute(Call.post("/openapi/v1/download/stop")
            .body(body)
            .idempotency(Call.Idempotency.REQUIRED)
            .slow()
            .as(DeviceDownloadCancel200Response.class));
    }

    /**
     * 回放会话列表
     *
     * <p>{@code GET /openapi/v1/playback/list}</p>
     *
     * <p>Required scope: {@code device:playback-list}</p>
     */
    public DevicePlaybackList200Response devicePlaybackList(String deviceId, String channelId, Integer page, Integer pageSize) {
        return invoker.execute(Call.get("/openapi/v1/playback/list")
            .query("deviceId", deviceId)
            .query("channelId", channelId)
            .query("page", page)
            .query("pageSize", pageSize)
            .as(DevicePlaybackList200Response.class));
    }

    /** Same as {@link #devicePlaybackList} with optional arguments left unset. */
    public DevicePlaybackList200Response devicePlaybackList(String deviceId, String channelId) {
        return devicePlaybackList(deviceId, channelId, null, null);
    }

    /**
     * 设备下载会话列表
     *
     * <p>{@code GET /openapi/v1/download/list}</p>
     *
     * <p>Required scope: {@code device:download-list}</p>
     */
    public DeviceDownloadList200Response deviceDownloadList(String deviceId, String channelId, Integer page, Integer pageSize) {
        return invoker.execute(Call.get("/openapi/v1/download/list")
            .query("deviceId", deviceId)
            .query("channelId", channelId)
            .query("page", page)
            .query("pageSize", pageSize)
            .as(DeviceDownloadList200Response.class));
    }

    /** Same as {@link #deviceDownloadList} with optional arguments left unset. */
    public DeviceDownloadList200Response deviceDownloadList(String deviceId, String channelId) {
        return deviceDownloadList(deviceId, channelId, null, null);
    }

    /**
     * 查询云端录像索引
     *
     * <p>{@code GET /openapi/v1/cloud/record/list}</p>
     *
     * <p>Required scope: {@code channel:cloud-record:list}</p>
     */
    public ListCloudRecords200Response listCloudRecords(String deviceId, String channelId, Integer page, Integer pageSize, String startTime, String endTime, String metadataState) {
        return invoker.execute(Call.get("/openapi/v1/cloud/record/list")
            .query("deviceId", deviceId)
            .query("channelId", channelId)
            .query("page", page)
            .query("pageSize", pageSize)
            .query("startTime", startTime)
            .query("endTime", endTime)
            .query("metadataState", metadataState)
            .as(ListCloudRecords200Response.class));
    }

    /** Same as {@link #listCloudRecords} with optional arguments left unset. */
    public ListCloudRecords200Response listCloudRecords(String deviceId, String channelId) {
        return listCloudRecords(deviceId, channelId, null, null, null, null, null);
    }

    /**
     * 读取云端录像索引详情
     *
     * <p>{@code GET /openapi/v1/cloud/record/info}</p>
     *
     * <p>Required scope: {@code channel:cloud-record:info}</p>
     */
    public GetCloudRecord200Response getCloudRecord(String deviceId, String channelId, String fileId) {
        return invoker.execute(Call.get("/openapi/v1/cloud/record/info")
            .query("deviceId", deviceId)
            .query("channelId", channelId)
            .query("fileId", fileId)
            .as(GetCloudRecord200Response.class));
    }

    /**
     * 查询有云端录像的日期
     *
     * <p>{@code GET /openapi/v1/cloud/record/dates}</p>
     *
     * <p>Required scope: {@code channel:cloud-record:dates}</p>
     */
    public GetCloudRecordDates200Response getCloudRecordDates(String deviceId, String channelId, String startDate, String endDate) {
        return invoker.execute(Call.get("/openapi/v1/cloud/record/dates")
            .query("deviceId", deviceId)
            .query("channelId", channelId)
            .query("startDate", startDate)
            .query("endDate", endDate)
            .as(GetCloudRecordDates200Response.class));
    }
}
