// GENERATED FILE - DO NOT EDIT.
// Source: contract/openapi-v1.yaml via java/codegen/generate_apis.py
package com.uvp.openapi.api;

import com.uvp.openapi.internal.ApiInvoker;
import com.uvp.openapi.internal.Call;
import com.uvp.openapi.model.*;

/**
 * Live preview. Returns playable URLs; the media plane is not handled by this SDK.
 *
 * <p>Signing, retries and error mapping happen inside {@link ApiInvoker}; no method here takes a
 * signature argument.</p>
 */
public final class LiveApi {

    private final ApiInvoker invoker;

    public LiveApi(ApiInvoker invoker) {
        this.invoker = invoker;
    }

    /**
     * Start or reuse live playback for one owned channel
     *
     * <p>{@code POST /openapi/v1/live/start}</p>
     *
     * <p>Required scope: {@code play:live}</p>
     */
    public StartLivePlayback200Response startLivePlayback(ActionStartLivePlaybackRequest body) {
        return invoker.execute(Call.post("/openapi/v1/live/start")
            .body(body)
            .slow()
            .as(StartLivePlayback200Response.class));
    }

    /**
     * Stop the live stream this client opened for one channel
     *
     * <p>{@code POST /openapi/v1/live/stop}</p>
     *
     * <p>Required scope: {@code play:live:stop}</p>
     */
    public LiveStopPlaybackResponse stopLivePlayback(ActionStopLivePlaybackRequest body) {
        return invoker.execute(Call.post("/openapi/v1/live/stop")
            .body(body)
            .slow()
            .as(LiveStopPlaybackResponse.class));
    }

    /**
     * 直播授权状态
     *
     * <p>{@code GET /openapi/v1/live/info}</p>
     *
     * <p>Required scope: {@code play:live:read}</p>
     */
    public GetLiveSession200Response getLiveSession(String deviceId, String channelId) {
        return invoker.execute(Call.get("/openapi/v1/live/info")
            .query("deviceId", deviceId)
            .query("channelId", channelId)
            .as(GetLiveSession200Response.class));
    }

    /**
     * 直播授权列表
     *
     * <p>{@code GET /openapi/v1/live/list}</p>
     *
     * <p>Required scope: {@code play:live:list}</p>
     */
    public ListLiveSessions200Response listLiveSessions(String deviceId, String channelId, Integer page, Integer pageSize) {
        return invoker.execute(Call.get("/openapi/v1/live/list")
            .query("deviceId", deviceId)
            .query("channelId", channelId)
            .query("page", page)
            .query("pageSize", pageSize)
            .as(ListLiveSessions200Response.class));
    }

    /** Same as {@link #listLiveSessions} with optional arguments left unset. */
    public ListLiveSessions200Response listLiveSessions(String deviceId, String channelId) {
        return listLiveSessions(deviceId, channelId, null, null);
    }

    /**
     * 请求关键帧
     *
     * <p>{@code POST /openapi/v1/live/keyframe}</p>
     *
     * <p>Required scope: {@code live:keyframe}</p>
     * <p>The platform requires an {@code Idempotency-Key} for this operation. One is
     * generated automatically; pin it with {@link com.uvp.openapi.client.UvpIdempotency}
     * when you retry after an unknown outcome.</p>
     */
    public RequestLiveKeyframe200Response requestLiveKeyframe(ActionRequestLiveKeyframeRequest body) {
        return invoker.execute(Call.post("/openapi/v1/live/keyframe")
            .body(body)
            .idempotency(Call.Idempotency.REQUIRED)
            .slow()
            .as(RequestLiveKeyframe200Response.class));
    }
}
