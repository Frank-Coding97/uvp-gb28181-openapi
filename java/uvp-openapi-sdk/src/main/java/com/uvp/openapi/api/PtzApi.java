// GENERATED FILE - DO NOT EDIT.
// Source: contract/openapi-v1.yaml via java/codegen/generate_apis.py
package com.uvp.openapi.api;

import com.uvp.openapi.internal.ApiInvoker;
import com.uvp.openapi.internal.Call;
import com.uvp.openapi.model.*;

/**
 * PTZ direction control, presets, cruise tracks and precise positioning.
 *
 * <p>Signing, retries and error mapping happen inside {@link ApiInvoker}; no method here takes a
 * signature argument.</p>
 */
public final class PtzApi {

    private final ApiInvoker invoker;

    public PtzApi(ApiInvoker invoker) {
        this.invoker = invoker;
    }

    /**
     * List PTZ presets for one owned channel
     *
     * <p>{@code GET /openapi/v1/ptz/preset/list}</p>
     *
     * <p>Required scope: {@code ptz:preset:list}</p>
     */
    public PTZPresetListResponse listPTZPresets(String deviceId, String channelId) {
        return invoker.execute(Call.get("/openapi/v1/ptz/preset/list")
            .query("deviceId", deviceId)
            .query("channelId", channelId)
            .as(PTZPresetListResponse.class));
    }

    /**
     * Save a PTZ preset on one owned channel
     *
     * <p>{@code POST /openapi/v1/ptz/preset/save}</p>
     *
     * <p>Required scope: {@code ptz:preset:save}</p>
     * <p>The platform requires an {@code Idempotency-Key} for this operation. One is
     * generated automatically; pin it with {@link com.uvp.openapi.client.UvpIdempotency}
     * when you retry after an unknown outcome.</p>
     */
    public PTZOperationResponse savePTZPreset(ActionSavePTZPresetRequest body) {
        return invoker.execute(Call.post("/openapi/v1/ptz/preset/save")
            .body(body)
            .idempotency(Call.Idempotency.REQUIRED)
            .slow()
            .as(PTZOperationResponse.class));
    }

    /**
     * Call a PTZ preset on one owned channel
     *
     * <p>{@code POST /openapi/v1/ptz/preset/call}</p>
     *
     * <p>Required scope: {@code ptz:preset:call}</p>
     * <p>The platform requires an {@code Idempotency-Key} for this operation. One is
     * generated automatically; pin it with {@link com.uvp.openapi.client.UvpIdempotency}
     * when you retry after an unknown outcome.</p>
     */
    public PTZOperationResponse callPTZPreset(ActionCallPTZPresetRequest body) {
        return invoker.execute(Call.post("/openapi/v1/ptz/preset/call")
            .body(body)
            .idempotency(Call.Idempotency.REQUIRED)
            .slow()
            .as(PTZOperationResponse.class));
    }

    /**
     * Delete a PTZ preset on one owned channel
     *
     * <p>{@code POST /openapi/v1/ptz/preset/delete}</p>
     *
     * <p>Required scope: {@code ptz:preset:delete}</p>
     * <p>The platform requires an {@code Idempotency-Key} for this operation. One is
     * generated automatically; pin it with {@link com.uvp.openapi.client.UvpIdempotency}
     * when you retry after an unknown outcome.</p>
     */
    public PTZOperationResponse deletePTZPreset(ActionDeletePTZPresetRequest body) {
        return invoker.execute(Call.post("/openapi/v1/ptz/preset/delete")
            .body(body)
            .idempotency(Call.Idempotency.REQUIRED)
            .slow()
            .as(PTZOperationResponse.class));
    }

    /**
     * Get one PTZ operation for the same owned channel
     *
     * <p>{@code GET /openapi/v1/ptz/operation/info}</p>
     *
     * <p>Required scope: {@code ptz:operation:read}</p>
     */
    public PTZOperationResponse getPTZOperation(String deviceId, String channelId, String operationId) {
        return invoker.execute(Call.get("/openapi/v1/ptz/operation/info")
            .query("deviceId", deviceId)
            .query("channelId", channelId)
            .query("operationId", operationId)
            .as(PTZOperationResponse.class));
    }

    /**
     * Read the latest cached precise PTZ status and observation time
     *
     * <p>{@code GET /openapi/v1/ptz/position}</p>
     *
     * <p>Required scope: {@code channel:precise-status}</p>
     */
    public PreciseStatusSnapshotResponse getPrecisePTZStatus(String deviceId, String channelId) {
        return invoker.execute(Call.get("/openapi/v1/ptz/position")
            .query("deviceId", deviceId)
            .query("channelId", channelId)
            .as(PreciseStatusSnapshotResponse.class));
    }

    /**
     * Read cached PTZ cruise tracks and freshness
     *
     * <p>{@code GET /openapi/v1/ptz/cruise/list}</p>
     *
     * <p>Required scope: {@code ptz:cruise:read}</p>
     */
    public CruiseTrackSnapshotResponse listPTZCruiseTracks(String deviceId, String channelId) {
        return invoker.execute(Call.get("/openapi/v1/ptz/cruise/list")
            .query("deviceId", deviceId)
            .query("channelId", channelId)
            .as(CruiseTrackSnapshotResponse.class));
    }

    /**
     * Create a PTZ cruise track with an ordered batch of device controls
     *
     * <p>{@code POST /openapi/v1/ptz/cruise/save}</p>
     *
     * <p>Required scope: {@code ptz:cruise:track:create}</p>
     * <p>The platform requires an {@code Idempotency-Key} for this operation. One is
     * generated automatically; pin it with {@link com.uvp.openapi.client.UvpIdempotency}
     * when you retry after an unknown outcome.</p>
     */
    public CruiseTrackCreateResponse createPTZCruiseTrack(ActionCreatePTZCruiseTrackRequest body) {
        return invoker.execute(Call.post("/openapi/v1/ptz/cruise/save")
            .body(body)
            .idempotency(Call.Idempotency.REQUIRED)
            .slow()
            .as(CruiseTrackCreateResponse.class));
    }

    /**
     * Read one cached PTZ cruise track and its freshness
     *
     * <p>{@code GET /openapi/v1/ptz/cruise/info}</p>
     *
     * <p>Required scope: {@code ptz:cruise:track:read}</p>
     */
    public CruiseTrackReadResponse getPTZCruiseTrack(String deviceId, String channelId, Integer trackId) {
        return invoker.execute(Call.get("/openapi/v1/ptz/cruise/info")
            .query("deviceId", deviceId)
            .query("channelId", channelId)
            .query("trackId", trackId)
            .as(CruiseTrackReadResponse.class));
    }

    /**
     * Read the latest target-track intent and reported capability
     *
     * <p>{@code GET /openapi/v1/ptz/track/info}</p>
     *
     * <p>Required scope: {@code channel:target-track:read}</p>
     */
    public TargetTrackSnapshotResponse getTargetTrackIntent(String deviceId, String channelId) {
        return invoker.execute(Call.get("/openapi/v1/ptz/track/info")
            .query("deviceId", deviceId)
            .query("channelId", channelId)
            .as(TargetTrackSnapshotResponse.class));
    }

    /**
     * Move a PTZ head or lens on one owned channel
     *
     * <p>{@code POST /openapi/v1/ptz/control}</p>
     *
     * <p>Required scope: {@code ptz:move}</p>
     * <p>The platform requires an {@code Idempotency-Key} for this operation. One is
     * generated automatically; pin it with {@link com.uvp.openapi.client.UvpIdempotency}
     * when you retry after an unknown outcome.</p>
     */
    public PTZControlOperationResponse movePTZ(ActionMovePTZRequest body) {
        return invoker.execute(Call.post("/openapi/v1/ptz/control")
            .body(body)
            .idempotency(Call.Idempotency.REQUIRED)
            .slow()
            .as(PTZControlOperationResponse.class));
    }

    /**
     * Send precise PTZ coordinates to a supported GB/T 28181-2022 device
     *
     * <p>{@code POST /openapi/v1/ptz/position/set}</p>
     *
     * <p>Required scope: {@code ptz:precise}</p>
     * <p>The platform requires an {@code Idempotency-Key} for this operation. One is
     * generated automatically; pin it with {@link com.uvp.openapi.client.UvpIdempotency}
     * when you retry after an unknown outcome.</p>
     */
    public PTZControlOperationResponse controlPrecisePTZ(ActionControlPrecisePTZRequest body) {
        return invoker.execute(Call.post("/openapi/v1/ptz/position/set")
            .body(body)
            .idempotency(Call.Idempotency.REQUIRED)
            .slow()
            .as(PTZControlOperationResponse.class));
    }

    /**
     * Control supported scan or cruise-point functions on one channel
     *
     * <p>{@code POST /openapi/v1/ptz/extended/control}</p>
     *
     * <p>Required scope: {@code ptz:extended}</p>
     * <p>The platform requires an {@code Idempotency-Key} for this operation. One is
     * generated automatically; pin it with {@link com.uvp.openapi.client.UvpIdempotency}
     * when you retry after an unknown outcome.</p>
     */
    public PTZControlOperationResponse controlExtendedPTZ(ActionControlExtendedPTZRequest body) {
        return invoker.execute(Call.post("/openapi/v1/ptz/extended/control")
            .body(body)
            .idempotency(Call.Idempotency.REQUIRED)
            .slow()
            .as(PTZControlOperationResponse.class));
    }

    /**
     * Turn a channel wiper on or off
     *
     * <p>{@code POST /openapi/v1/ptz/wiper/set}</p>
     *
     * <p>Required scope: {@code ptz:wiper}</p>
     * <p>The platform requires an {@code Idempotency-Key} for this operation. One is
     * generated automatically; pin it with {@link com.uvp.openapi.client.UvpIdempotency}
     * when you retry after an unknown outcome.</p>
     */
    public PTZControlOperationResponse controlPTZWiper(ActionControlPTZWiperRequest body) {
        return invoker.execute(Call.post("/openapi/v1/ptz/wiper/set")
            .body(body)
            .idempotency(Call.Idempotency.REQUIRED)
            .slow()
            .as(PTZControlOperationResponse.class));
    }

    /**
     * Read cached home-position state and verification source
     *
     * <p>{@code GET /openapi/v1/ptz/home/info}</p>
     *
     * <p>Required scope: {@code ptz:home-position:read}</p>
     */
    public HomePositionSnapshotResponse getPTZHomePosition(String deviceId, String channelId) {
        return invoker.execute(Call.get("/openapi/v1/ptz/home/info")
            .query("deviceId", deviceId)
            .query("channelId", channelId)
            .as(HomePositionSnapshotResponse.class));
    }

    /**
     * Set or disable a channel home position
     *
     * <p>{@code POST /openapi/v1/ptz/home/set}</p>
     *
     * <p>Required scope: {@code ptz:home-position:set}</p>
     * <p>The platform requires an {@code Idempotency-Key} for this operation. One is
     * generated automatically; pin it with {@link com.uvp.openapi.client.UvpIdempotency}
     * when you retry after an unknown outcome.</p>
     */
    public PTZControlOperationResponse setPTZHomePosition(ActionSetPTZHomePositionRequest body) {
        return invoker.execute(Call.post("/openapi/v1/ptz/home/set")
            .body(body)
            .idempotency(Call.Idempotency.REQUIRED)
            .slow()
            .as(PTZControlOperationResponse.class));
    }

    /**
     * Start, stop, or delete a cruise path on one owned channel
     *
     * <p>{@code POST /openapi/v1/ptz/cruise/control}</p>
     *
     * <p>Required scope: {@code ptz:cruise:control}</p>
     * <p>The platform requires an {@code Idempotency-Key} for this operation. One is
     * generated automatically; pin it with {@link com.uvp.openapi.client.UvpIdempotency}
     * when you retry after an unknown outcome.</p>
     */
    public PTZControlOperationResponse controlPTZCruise(ActionControlPTZCruiseRequest body) {
        return invoker.execute(Call.post("/openapi/v1/ptz/cruise/control")
            .body(body)
            .idempotency(Call.Idempotency.REQUIRED)
            .slow()
            .as(PTZControlOperationResponse.class));
    }

    /**
     * Send a target-tracking command to a supporting GB/T 28181-2022 device
     *
     * <p>{@code POST /openapi/v1/ptz/track/set}</p>
     *
     * <p>Required scope: {@code ptz:target-track}</p>
     * <p>The platform requires an {@code Idempotency-Key} for this operation. One is
     * generated automatically; pin it with {@link com.uvp.openapi.client.UvpIdempotency}
     * when you retry after an unknown outcome.</p>
     */
    public PTZTargetTrackResponse setPTZTargetTrack(ActionSetPTZTargetTrackRequest body) {
        return invoker.execute(Call.post("/openapi/v1/ptz/track/set")
            .body(body)
            .idempotency(Call.Idempotency.REQUIRED)
            .slow()
            .as(PTZTargetTrackResponse.class));
    }

    /**
     * 主动查询设备预置位
     *
     * <p>{@code POST /openapi/v1/ptz/preset/refresh}</p>
     *
     * <p>Required scope: {@code channel:ptz:preset:refresh}</p>
     * <p>The platform requires an {@code Idempotency-Key} for this operation. One is
     * generated automatically; pin it with {@link com.uvp.openapi.client.UvpIdempotency}
     * when you retry after an unknown outcome.</p>
     */
    public RefreshChannelPreset200Response refreshChannelPreset(ActionRefreshChannelPresetRequest body) {
        return invoker.execute(Call.post("/openapi/v1/ptz/preset/refresh")
            .body(body)
            .idempotency(Call.Idempotency.REQUIRED)
            .slow()
            .as(RefreshChannelPreset200Response.class));
    }

    /**
     * 主动查询设备看守位
     *
     * <p>{@code POST /openapi/v1/ptz/home/refresh}</p>
     *
     * <p>Required scope: {@code channel:ptz:home:refresh}</p>
     * <p>The platform requires an {@code Idempotency-Key} for this operation. One is
     * generated automatically; pin it with {@link com.uvp.openapi.client.UvpIdempotency}
     * when you retry after an unknown outcome.</p>
     */
    public RefreshChannelHome200Response refreshChannelHome(ActionRefreshChannelHomeRequest body) {
        return invoker.execute(Call.post("/openapi/v1/ptz/home/refresh")
            .body(body)
            .idempotency(Call.Idempotency.REQUIRED)
            .slow()
            .as(RefreshChannelHome200Response.class));
    }

    /**
     * 主动查询设备巡航轨迹
     *
     * <p>{@code POST /openapi/v1/ptz/cruise/refresh}</p>
     *
     * <p>Required scope: {@code channel:ptz:cruise:refresh}</p>
     * <p>The platform requires an {@code Idempotency-Key} for this operation. One is
     * generated automatically; pin it with {@link com.uvp.openapi.client.UvpIdempotency}
     * when you retry after an unknown outcome.</p>
     */
    public RefreshChannelCruise200Response refreshChannelCruise(ActionRefreshChannelCruiseRequest body) {
        return invoker.execute(Call.post("/openapi/v1/ptz/cruise/refresh")
            .body(body)
            .idempotency(Call.Idempotency.REQUIRED)
            .slow()
            .as(RefreshChannelCruise200Response.class));
    }

    /**
     * 主动查询精准云台位置
     *
     * <p>{@code POST /openapi/v1/ptz/position/refresh}</p>
     *
     * <p>Required scope: {@code channel:ptz:position:refresh}</p>
     * <p>The platform requires an {@code Idempotency-Key} for this operation. One is
     * generated automatically; pin it with {@link com.uvp.openapi.client.UvpIdempotency}
     * when you retry after an unknown outcome.</p>
     */
    public RefreshChannelPosition200Response refreshChannelPosition(ActionRefreshChannelPositionRequest body) {
        return invoker.execute(Call.post("/openapi/v1/ptz/position/refresh")
            .body(body)
            .idempotency(Call.Idempotency.REQUIRED)
            .slow()
            .as(RefreshChannelPosition200Response.class));
    }

    /**
     * 拉框放大缩小
     *
     * <p>{@code POST /openapi/v1/ptz/zoom/drag}</p>
     *
     * <p>Required scope: {@code ptz:zoom:drag}</p>
     * <p>The platform requires an {@code Idempotency-Key} for this operation. One is
     * generated automatically; pin it with {@link com.uvp.openapi.client.UvpIdempotency}
     * when you retry after an unknown outcome.</p>
     */
    public DragZoomPTZ200Response dragZoomPTZ(ActionDragZoomPTZRequest body) {
        return invoker.execute(Call.post("/openapi/v1/ptz/zoom/drag")
            .body(body)
            .idempotency(Call.Idempotency.REQUIRED)
            .slow()
            .as(DragZoomPTZ200Response.class));
    }
}
