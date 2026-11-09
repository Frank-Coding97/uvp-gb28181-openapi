// GENERATED FILE - DO NOT EDIT.
// Source: contract/openapi-v1.yaml via java/codegen/generate_apis.py
package com.uvp.openapi.api;

import com.uvp.openapi.internal.ApiInvoker;
import com.uvp.openapi.internal.Call;
import com.uvp.openapi.model.*;

/**
 * Devices, channels, live status and storage cards.
 *
 * <p>Signing, retries and error mapping happen inside {@link ApiInvoker}; no method here takes a
 * signature argument.</p>
 */
public final class DeviceApi {

    private final ApiInvoker invoker;

    public DeviceApi(ApiInvoker invoker) {
        this.invoker = invoker;
    }

    /**
     * List devices owned by the authenticated machine client
     *
     * <p>{@code GET /openapi/v1/device/list}</p>
     *
     * <p>Required scope: {@code device:list}</p>
     */
    public DeviceListResponse listDevices(Integer page, Integer pageSize, String keyword, String status, String manufacturer, String model, String transport, String protocolVersion, String sort, String order) {
        return invoker.execute(Call.get("/openapi/v1/device/list")
            .query("page", page)
            .query("pageSize", pageSize)
            .query("keyword", keyword)
            .query("status", status)
            .query("manufacturer", manufacturer)
            .query("model", model)
            .query("transport", transport)
            .query("protocolVersion", protocolVersion)
            .query("sort", sort)
            .query("order", order)
            .as(DeviceListResponse.class));
    }

    /** Same as {@link #listDevices} with optional arguments left unset. */
    public DeviceListResponse listDevices() {
        return listDevices(null, null, null, null, null, null, null, null, null, null);
    }

    /**
     * Get one device owned by the authenticated machine client
     *
     * <p>{@code GET /openapi/v1/device/info}</p>
     *
     * <p>Required scope: {@code device:detail}</p>
     */
    public DeviceResponse getDevice(String deviceId) {
        return invoker.execute(Call.get("/openapi/v1/device/info")
            .query("deviceId", deviceId)
            .as(DeviceResponse.class));
    }

    /**
     * Get the status of one owned device
     *
     * <p>{@code GET /openapi/v1/device/status}</p>
     *
     * <p>Required scope: {@code device:status}</p>
     */
    public DeviceStatusResponse getDeviceStatus(String deviceId) {
        return invoker.execute(Call.get("/openapi/v1/device/status")
            .query("deviceId", deviceId)
            .as(DeviceStatusResponse.class));
    }

    /**
     * List channels of one owned device
     *
     * <p>{@code GET /openapi/v1/channel/list}</p>
     *
     * <p>Required scope: {@code channel:list}</p>
     */
    public ChannelListResponse listChannels(String deviceId, Integer page, Integer pageSize, String keyword, String status, String manufacturer, String model, Integer ptzType, Boolean audioEnabled, String streamTransport, String sort, String order) {
        return invoker.execute(Call.get("/openapi/v1/channel/list")
            .query("deviceId", deviceId)
            .query("page", page)
            .query("pageSize", pageSize)
            .query("keyword", keyword)
            .query("status", status)
            .query("manufacturer", manufacturer)
            .query("model", model)
            .query("ptzType", ptzType)
            .query("audioEnabled", audioEnabled)
            .query("streamTransport", streamTransport)
            .query("sort", sort)
            .query("order", order)
            .as(ChannelListResponse.class));
    }

    /** Same as {@link #listChannels} with optional arguments left unset. */
    public ChannelListResponse listChannels(String deviceId) {
        return listChannels(deviceId, null, null, null, null, null, null, null, null, null, null, null);
    }

    /**
     * Get one channel of one owned device
     *
     * <p>{@code GET /openapi/v1/channel/info}</p>
     *
     * <p>Required scope: {@code channel:detail}</p>
     */
    public ChannelResponse getChannel(String deviceId, String channelId) {
        return invoker.execute(Call.get("/openapi/v1/channel/info")
            .query("deviceId", deviceId)
            .query("channelId", channelId)
            .as(ChannelResponse.class));
    }

    /**
     * Get the status of one owned channel
     *
     * <p>{@code GET /openapi/v1/channel/status}</p>
     *
     * <p>Required scope: {@code channel:status}</p>
     */
    public ChannelStatusResponse getChannelStatus(String deviceId, String channelId) {
        return invoker.execute(Call.get("/openapi/v1/channel/status")
            .query("deviceId", deviceId)
            .query("channelId", channelId)
            .as(ChannelStatusResponse.class));
    }

    /**
     * Get explicitly reported control capabilities for one owned channel
     *
     * <p>{@code GET /openapi/v1/channel/capabilities}</p>
     *
     * <p>Required scope: {@code channel:capabilities}</p>
     */
    public ControlCapabilitiesResponse getChannelControlCapabilities(String deviceId, String channelId) {
        return invoker.execute(Call.get("/openapi/v1/channel/capabilities")
            .query("deviceId", deviceId)
            .query("channelId", channelId)
            .as(ControlCapabilitiesResponse.class));
    }

    /**
     * 手动刷新设备目录
     *
     * <p>{@code POST /openapi/v1/device/catalog/refresh}</p>
     *
     * <p>Required scope: {@code device:catalog:refresh}</p>
     */
    public DeviceCatalogRefreshResponse refreshDeviceCatalog(ActionRefreshDeviceCatalogRequest body) {
        return invoker.execute(Call.post("/openapi/v1/device/catalog/refresh")
            .body(body)
            .slow()
            .as(DeviceCatalogRefreshResponse.class));
    }

    /**
     * 读取目录刷新进度
     *
     * <p>{@code GET /openapi/v1/device/catalog/progress}</p>
     *
     * <p>Required scope: {@code device:catalog:refresh:read}</p>
     */
    public DeviceCatalogRefreshProgressResponse getDeviceCatalogRefreshProgress(String deviceId, String operationId) {
        return invoker.execute(Call.get("/openapi/v1/device/catalog/progress")
            .query("deviceId", deviceId)
            .query("operationId", operationId)
            .as(DeviceCatalogRefreshProgressResponse.class));
    }

    /**
     * 通道目录挂载位置
     *
     * <p>{@code GET /openapi/v1/channel/mount/list}</p>
     *
     * <p>Required scope: {@code channel:mounts:read}</p>
     */
    public OpenAPIChannelMountListResponse channelMountList(String deviceId, String channelId) {
        return invoker.execute(Call.get("/openapi/v1/channel/mount/list")
            .query("deviceId", deviceId)
            .query("channelId", channelId)
            .as(OpenAPIChannelMountListResponse.class));
    }

    /**
     * 通道 24 小时在线时序
     *
     * <p>{@code GET /openapi/v1/channel/timeline}</p>
     *
     * <p>Required scope: {@code channel:timeline:read}</p>
     */
    public OpenAPIChannelTimelineResponse channelTimeline(String deviceId, String channelId) {
        return invoker.execute(Call.get("/openapi/v1/channel/timeline")
            .query("deviceId", deviceId)
            .query("channelId", channelId)
            .as(OpenAPIChannelTimelineResponse.class));
    }

    /**
     * 设备抓图
     *
     * <p>{@code POST /openapi/v1/snapshot/start}</p>
     *
     * <p>Required scope: {@code channel:snapshot:create}</p>
     * <p>The platform requires an {@code Idempotency-Key} for this operation. One is
     * generated automatically; pin it with {@link com.uvp.openapi.client.UvpIdempotency}
     * when you retry after an unknown outcome.</p>
     */
    public CreateChannelSnapshotSession200Response createChannelSnapshotSession(ActionCreateChannelSnapshotSessionRequest body) {
        return invoker.execute(Call.post("/openapi/v1/snapshot/start")
            .body(body)
            .idempotency(Call.Idempotency.REQUIRED)
            .slow()
            .as(CreateChannelSnapshotSession200Response.class));
    }

    /**
     * 读取抓图会话
     *
     * <p>{@code GET /openapi/v1/snapshot/info}</p>
     *
     * <p>Required scope: {@code channel:snapshot:read}</p>
     */
    public GetChannelSnapshotSession200Response getChannelSnapshotSession(String deviceId, String channelId, String sessionId) {
        return invoker.execute(Call.get("/openapi/v1/snapshot/info")
            .query("deviceId", deviceId)
            .query("channelId", channelId)
            .query("sessionId", sessionId)
            .as(GetChannelSnapshotSession200Response.class));
    }

    /**
     * 读取抓图图像内容
     *
     * <p>{@code GET /openapi/v1/snapshot/image/content}</p>
     *
     * <p>Required scope: {@code channel:snapshot:image:read}</p>
     */
    public GetChannelSnapshotImageContent200Response getChannelSnapshotImageContent(String deviceId, String channelId, String sessionId, String fileName) {
        return invoker.execute(Call.get("/openapi/v1/snapshot/image/content")
            .query("deviceId", deviceId)
            .query("channelId", channelId)
            .query("sessionId", sessionId)
            .query("fileName", fileName)
            .as(GetChannelSnapshotImageContent200Response.class));
    }

    /**
     * 通道抓拍图列表
     *
     * <p>{@code GET /openapi/v1/snapshot/library/list}</p>
     *
     * <p>Required scope: {@code channel:snapshot:library:read}</p>
     */
    public ListChannelSnapshotLibrary200Response listChannelSnapshotLibrary(String deviceId, String channelId, Integer page, Integer pageSize, String from, String to, String source) {
        return invoker.execute(Call.get("/openapi/v1/snapshot/library/list")
            .query("deviceId", deviceId)
            .query("channelId", channelId)
            .query("page", page)
            .query("pageSize", pageSize)
            .query("from", from)
            .query("to", to)
            .query("source", source)
            .as(ListChannelSnapshotLibrary200Response.class));
    }

    /** Same as {@link #listChannelSnapshotLibrary} with optional arguments left unset. */
    public ListChannelSnapshotLibrary200Response listChannelSnapshotLibrary(String deviceId, String channelId) {
        return listChannelSnapshotLibrary(deviceId, channelId, null, null, null, null, null);
    }

    /**
     * 批量删除抓拍图
     *
     * <p>{@code POST /openapi/v1/snapshot/library/delete}</p>
     *
     * <p>Required scope: {@code channel:snapshot:library:delete}</p>
     */
    public DeleteChannelSnapshotLibrary200Response deleteChannelSnapshotLibrary(ActionDeleteChannelSnapshotLibraryRequest body) {
        return invoker.execute(Call.post("/openapi/v1/snapshot/library/delete")
            .body(body)
            .idempotency(Call.Idempotency.OPTIONAL)
            .slow()
            .as(DeleteChannelSnapshotLibrary200Response.class));
    }

    /**
     * 读取抓拍图内容
     *
     * <p>{@code GET /openapi/v1/snapshot/library/content}</p>
     *
     * <p>Required scope: {@code channel:snapshot:library:image:read}</p>
     */
    public GetChannelSnapshotLibraryImageContent200Response getChannelSnapshotLibraryImageContent(String deviceId, String channelId, String snapshotId) {
        return invoker.execute(Call.get("/openapi/v1/snapshot/library/content")
            .query("deviceId", deviceId)
            .query("channelId", channelId)
            .query("snapshotId", snapshotId)
            .as(GetChannelSnapshotLibraryImageContent200Response.class));
    }

    /**
     * 设备统计
     *
     * <p>{@code GET /openapi/v1/device/statistics}</p>
     *
     * <p>Required scope: {@code device:statistics}</p>
     */
    public DeviceStatistics200Response deviceStatistics(String keyword, String status, String manufacturer, String model, String transport, String protocolVersion) {
        return invoker.execute(Call.get("/openapi/v1/device/statistics")
            .query("keyword", keyword)
            .query("status", status)
            .query("manufacturer", manufacturer)
            .query("model", model)
            .query("transport", transport)
            .query("protocolVersion", protocolVersion)
            .as(DeviceStatistics200Response.class));
    }

    /** Same as {@link #deviceStatistics} with optional arguments left unset. */
    public DeviceStatistics200Response deviceStatistics() {
        return deviceStatistics(null, null, null, null, null, null);
    }

    /**
     * 通道统计
     *
     * <p>{@code GET /openapi/v1/channel/statistics}</p>
     *
     * <p>Required scope: {@code channel:statistics}</p>
     */
    public ChannelStatistics200Response channelStatistics(String deviceId, String keyword, String status, String manufacturer, String model, Integer ptzType, Boolean audioEnabled, String streamTransport) {
        return invoker.execute(Call.get("/openapi/v1/channel/statistics")
            .query("deviceId", deviceId)
            .query("keyword", keyword)
            .query("status", status)
            .query("manufacturer", manufacturer)
            .query("model", model)
            .query("ptzType", ptzType)
            .query("audioEnabled", audioEnabled)
            .query("streamTransport", streamTransport)
            .as(ChannelStatistics200Response.class));
    }

    /** Same as {@link #channelStatistics} with optional arguments left unset. */
    public ChannelStatistics200Response channelStatistics(String deviceId) {
        return channelStatistics(deviceId, null, null, null, null, null, null, null);
    }

    /**
     * 编辑设备
     *
     * <p>{@code POST /openapi/v1/device/update}</p>
     *
     * <p>Required scope: {@code device:update}</p>
     */
    public DeviceUpdate200Response deviceUpdate(ActionDeviceUpdateRequest body) {
        return invoker.execute(Call.post("/openapi/v1/device/update")
            .body(body)
            .slow()
            .as(DeviceUpdate200Response.class));
    }

    /**
     * 编辑通道
     *
     * <p>{@code POST /openapi/v1/channel/update}</p>
     *
     * <p>Required scope: {@code channel:update}</p>
     */
    public ChannelUpdate200Response channelUpdate(ActionChannelUpdateRequest body) {
        return invoker.execute(Call.post("/openapi/v1/channel/update")
            .body(body)
            .slow()
            .as(ChannelUpdate200Response.class));
    }

    /**
     * 主动查询录像或报警控制状态
     *
     * <p>{@code POST /openapi/v1/channel/status/refresh}</p>
     *
     * <p>Required scope: {@code channel:control-status:refresh}</p>
     * <p>The platform requires an {@code Idempotency-Key} for this operation. One is
     * generated automatically; pin it with {@link com.uvp.openapi.client.UvpIdempotency}
     * when you retry after an unknown outcome.</p>
     */
    public RefreshChannelStatus200Response refreshChannelStatus(ActionRefreshChannelStatusRequest body) {
        return invoker.execute(Call.post("/openapi/v1/channel/status/refresh")
            .body(body)
            .idempotency(Call.Idempotency.REQUIRED)
            .slow()
            .as(RefreshChannelStatus200Response.class));
    }

    /**
     * 设备主动查询操作状态
     *
     * <p>{@code GET /openapi/v1/query/operation/info}</p>
     *
     * <p>Required scope: {@code channel:query:operation:read}</p>
     */
    public GetChannelQueryOperation200Response getChannelQueryOperation(String deviceId, String channelId, String operationId) {
        return invoker.execute(Call.get("/openapi/v1/query/operation/info")
            .query("deviceId", deviceId)
            .query("channelId", channelId)
            .query("operationId", operationId)
            .as(GetChannelQueryOperation200Response.class));
    }

    /**
     * 读取录像及报警控制状态
     *
     * <p>{@code GET /openapi/v1/channel/control/status}</p>
     *
     * <p>Required scope: {@code channel:control-status:read}</p>
     */
    public GetChannelControlStatus200Response getChannelControlStatus(String deviceId, String channelId) {
        return invoker.execute(Call.get("/openapi/v1/channel/control/status")
            .query("deviceId", deviceId)
            .query("channelId", channelId)
            .as(GetChannelControlStatus200Response.class));
    }
}
