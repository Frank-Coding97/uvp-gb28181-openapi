// GENERATED FILE - DO NOT EDIT.
// Source: contract/openapi-v1.yaml via java/codegen/generate_apis.py
package com.uvp.openapi.api;

import com.uvp.openapi.internal.ApiInvoker;
import com.uvp.openapi.internal.Call;
import com.uvp.openapi.model.*;

/**
 * Voice talk and broadcast. SDP is passed through; media stays outside the SDK.
 *
 * <p>Signing, retries and error mapping happen inside {@link ApiInvoker}; no method here takes a
 * signature argument.</p>
 */
public final class TalkApi {

    private final ApiInvoker invoker;

    public TalkApi(ApiInvoker invoker) {
        this.invoker = invoker;
    }

    /**
     * Create device talk session
     *
     * <p>{@code POST /openapi/v1/talk/start}</p>
     *
     * <p>Required scope: {@code device:talk:create}</p>
     * <p>The platform requires an {@code Idempotency-Key} for this operation. One is
     * generated automatically; pin it with {@link com.uvp.openapi.client.UvpIdempotency}
     * when you retry after an unknown outcome.</p>
     */
    public DeviceTalkCreate200Response deviceTalkCreate(ActionDeviceTalkCreateRequest body) {
        return invoker.execute(Call.post("/openapi/v1/talk/start")
            .body(body)
            .idempotency(Call.Idempotency.REQUIRED)
            .slow()
            .as(DeviceTalkCreate200Response.class));
    }

    /**
     * Read device talk session
     *
     * <p>{@code GET /openapi/v1/talk/info}</p>
     *
     * <p>Required scope: {@code device:talk:read}</p>
     */
    public DeviceTalkRead200Response deviceTalkRead(String deviceId, String channelId, String sessionId) {
        return invoker.execute(Call.get("/openapi/v1/talk/info")
            .query("deviceId", deviceId)
            .query("channelId", channelId)
            .query("sessionId", sessionId)
            .as(DeviceTalkRead200Response.class));
    }

    /**
     * End device talk session
     *
     * <p>{@code POST /openapi/v1/talk/stop}</p>
     *
     * <p>Required scope: {@code device:talk:end}</p>
     */
    public DeviceTalkEnd200Response deviceTalkEnd(ActionDeviceTalkEndRequest body) {
        return invoker.execute(Call.post("/openapi/v1/talk/stop")
            .body(body)
            .slow()
            .as(DeviceTalkEnd200Response.class));
    }

    /**
     * Create device broadcast session
     *
     * <p>{@code POST /openapi/v1/broadcast/start}</p>
     *
     * <p>Required scope: {@code device:broadcast:create}</p>
     * <p>The platform requires an {@code Idempotency-Key} for this operation. One is
     * generated automatically; pin it with {@link com.uvp.openapi.client.UvpIdempotency}
     * when you retry after an unknown outcome.</p>
     */
    public DeviceBroadcastCreate200Response deviceBroadcastCreate(ActionDeviceBroadcastCreateRequest body) {
        return invoker.execute(Call.post("/openapi/v1/broadcast/start")
            .body(body)
            .idempotency(Call.Idempotency.REQUIRED)
            .slow()
            .as(DeviceBroadcastCreate200Response.class));
    }

    /**
     * Read device broadcast session
     *
     * <p>{@code GET /openapi/v1/broadcast/info}</p>
     *
     * <p>Required scope: {@code device:broadcast:read}</p>
     */
    public DeviceBroadcastRead200Response deviceBroadcastRead(String deviceId, String channelId, String sessionId) {
        return invoker.execute(Call.get("/openapi/v1/broadcast/info")
            .query("deviceId", deviceId)
            .query("channelId", channelId)
            .query("sessionId", sessionId)
            .as(DeviceBroadcastRead200Response.class));
    }

    /**
     * End device broadcast session
     *
     * <p>{@code POST /openapi/v1/broadcast/stop}</p>
     *
     * <p>Required scope: {@code device:broadcast:end}</p>
     */
    public DeviceBroadcastEnd200Response deviceBroadcastEnd(ActionDeviceBroadcastEndRequest body) {
        return invoker.execute(Call.post("/openapi/v1/broadcast/stop")
            .body(body)
            .slow()
            .as(DeviceBroadcastEnd200Response.class));
    }

    /**
     * Submit device talk media offer
     *
     * <p>{@code POST /openapi/v1/talk/media}</p>
     *
     * <p>Required scope: {@code device:talk:media}</p>
     */
    public VoiceMediaAnswerResponse deviceTalkMedia(ActionDeviceTalkMediaRequest body) {
        return invoker.execute(Call.post("/openapi/v1/talk/media")
            .body(body)
            .slow()
            .as(VoiceMediaAnswerResponse.class));
    }

    /**
     * Submit device broadcast media offer
     *
     * <p>{@code POST /openapi/v1/broadcast/media}</p>
     *
     * <p>Required scope: {@code device:broadcast:media}</p>
     */
    public VoiceMediaAnswerResponse deviceBroadcastMedia(ActionDeviceBroadcastMediaRequest body) {
        return invoker.execute(Call.post("/openapi/v1/broadcast/media")
            .body(body)
            .slow()
            .as(VoiceMediaAnswerResponse.class));
    }
}
