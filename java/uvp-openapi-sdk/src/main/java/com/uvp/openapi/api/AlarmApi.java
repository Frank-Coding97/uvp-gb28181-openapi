// GENERATED FILE - DO NOT EDIT.
// Source: contract/openapi-v1.yaml via java/codegen/generate_apis.py
package com.uvp.openapi.api;

import com.uvp.openapi.internal.ApiInvoker;
import com.uvp.openapi.internal.Call;
import com.uvp.openapi.model.*;

/**
 * Alarms, status events, subscriptions and reported positions.
 *
 * <p>Signing, retries and error mapping happen inside {@link ApiInvoker}; no method here takes a
 * signature argument.</p>
 */
public final class AlarmApi {

    private final ApiInvoker invoker;

    public AlarmApi(ApiInvoker invoker) {
        this.invoker = invoker;
    }

    /**
     * List persisted alarms for one owned device
     *
     * <p>{@code GET /openapi/v1/alarm/list}</p>
     *
     * <p>Required scope: {@code device:alarms}</p>
     */
    public ListDeviceAlarms200Response listDeviceAlarms(String deviceId, Integer page, Integer pageSize, String keyword, String sourceCode, String alarmFrom, String alarmTo, Integer priority, Integer method, Integer alarmType) {
        return invoker.execute(Call.get("/openapi/v1/alarm/list")
            .query("deviceId", deviceId)
            .query("page", page)
            .query("pageSize", pageSize)
            .query("keyword", keyword)
            .query("sourceCode", sourceCode)
            .query("alarmFrom", alarmFrom)
            .query("alarmTo", alarmTo)
            .query("priority", priority)
            .query("method", method)
            .query("alarmType", alarmType)
            .as(ListDeviceAlarms200Response.class));
    }

    /** Same as {@link #listDeviceAlarms} with optional arguments left unset. */
    public ListDeviceAlarms200Response listDeviceAlarms(String deviceId) {
        return listDeviceAlarms(deviceId, null, null, null, null, null, null, null, null, null);
    }

    /**
     * Continue reading device status events with an opaque cursor
     *
     * <p>{@code GET /openapi/v1/event/status/list}</p>
     *
     * <p>Required scope: {@code device:status-events}</p>
     */
    public DeviceStatusEventPageResponse listDeviceStatusEvents(String deviceId, String cursor, Integer pageSize) {
        return invoker.execute(Call.get("/openapi/v1/event/status/list")
            .query("deviceId", deviceId)
            .query("cursor", cursor)
            .query("pageSize", pageSize)
            .as(DeviceStatusEventPageResponse.class));
    }

    /** Same as {@link #listDeviceStatusEvents} with optional arguments left unset. */
    public DeviceStatusEventPageResponse listDeviceStatusEvents(String deviceId) {
        return listDeviceStatusEvents(deviceId, null, null);
    }

    /**
     * Create a client-owned subscription to persisted device events
     *
     * <p>{@code POST /openapi/v1/subscription/create}</p>
     *
     * <p>Required scope: {@code device:subscription:create}</p>
     */
    public DeviceSubscriptionResponse deviceSubscriptionCreate(ActionDeviceSubscriptionCreateRequest body) {
        return invoker.execute(Call.post("/openapi/v1/subscription/create")
            .body(body)
            .slow()
            .as(DeviceSubscriptionResponse.class));
    }

    /**
     * List the client-owned subscriptions of one device
     *
     * <p>{@code GET /openapi/v1/subscription/list}</p>
     *
     * <p>Required scope: {@code device:subscription:list}</p>
     */
    public DeviceSubscriptionListResponse deviceSubscriptionList(String deviceId) {
        return invoker.execute(Call.get("/openapi/v1/subscription/list")
            .query("deviceId", deviceId)
            .as(DeviceSubscriptionListResponse.class));
    }

    /**
     * Read a subscription owned by the authenticated client
     *
     * <p>{@code GET /openapi/v1/subscription/info}</p>
     *
     * <p>Required scope: {@code device:subscription:read}</p>
     */
    public DeviceSubscriptionResponse deviceSubscriptionRead(String deviceId, String subscriptionId) {
        return invoker.execute(Call.get("/openapi/v1/subscription/info")
            .query("deviceId", deviceId)
            .query("subscriptionId", subscriptionId)
            .as(DeviceSubscriptionResponse.class));
    }

    /**
     * Cancel this client's logical event subscription
     *
     * <p>{@code POST /openapi/v1/subscription/cancel}</p>
     *
     * <p>Required scope: {@code device:subscription:cancel}</p>
     */
    public DeviceSubscriptionResponse deviceSubscriptionCancel(ActionDeviceSubscriptionCancelRequest body) {
        return invoker.execute(Call.post("/openapi/v1/subscription/cancel")
            .body(body)
            .slow()
            .as(DeviceSubscriptionResponse.class));
    }

    /**
     * Renew an active device event subscription
     *
     * <p>{@code POST /openapi/v1/subscription/renew}</p>
     *
     * <p>Required scope: {@code device:subscription:renew}</p>
     */
    public DeviceSubscriptionResponse deviceSubscriptionRenew(ActionDeviceSubscriptionRenewRequest body) {
        return invoker.execute(Call.post("/openapi/v1/subscription/renew")
            .body(body)
            .slow()
            .as(DeviceSubscriptionResponse.class));
    }

    /**
     * Continue reading persisted events with a signed opaque cursor
     *
     * <p>{@code GET /openapi/v1/event/list}</p>
     *
     * <p>Required scope: {@code device:events:read}</p>
     */
    public DeviceEventPageResponse deviceEventsRead(String deviceId, String subscriptionId, String cursor, Integer pageSize) {
        return invoker.execute(Call.get("/openapi/v1/event/list")
            .query("deviceId", deviceId)
            .query("subscriptionId", subscriptionId)
            .query("cursor", cursor)
            .query("pageSize", pageSize)
            .as(DeviceEventPageResponse.class));
    }

    /** Same as {@link #deviceEventsRead} with optional arguments left unset. */
    public DeviceEventPageResponse deviceEventsRead(String deviceId, String subscriptionId) {
        return deviceEventsRead(deviceId, subscriptionId, null, null);
    }

    /**
     * Read the latest platform-collected positions for a device
     *
     * <p>{@code GET /openapi/v1/position/latest}</p>
     *
     * <p>Required scope: {@code device:position:read}</p>
     */
    public DevicePositionPageResponse devicePositionRead(String deviceId) {
        return invoker.execute(Call.get("/openapi/v1/position/latest")
            .query("deviceId", deviceId)
            .as(DevicePositionPageResponse.class));
    }

    /**
     * Continue reading persisted mobile position history
     *
     * <p>{@code GET /openapi/v1/position/history}</p>
     *
     * <p>Required scope: {@code device:position:history:read}</p>
     */
    public DeviceEventPageResponse devicePositionHistoryRead(String deviceId, String cursor, Integer pageSize) {
        return invoker.execute(Call.get("/openapi/v1/position/history")
            .query("deviceId", deviceId)
            .query("cursor", cursor)
            .query("pageSize", pageSize)
            .as(DeviceEventPageResponse.class));
    }

    /** Same as {@link #devicePositionHistoryRead} with optional arguments left unset. */
    public DeviceEventPageResponse devicePositionHistoryRead(String deviceId) {
        return devicePositionHistoryRead(deviceId, null, null);
    }

    /**
     * 设备报警详情
     *
     * <p>{@code GET /openapi/v1/alarm/info}</p>
     *
     * <p>Required scope: {@code device:alarm:detail}</p>
     */
    public GetDeviceAlarm200Response getDeviceAlarm(String deviceId, String alarmId) {
        return invoker.execute(Call.get("/openapi/v1/alarm/info")
            .query("deviceId", deviceId)
            .query("alarmId", alarmId)
            .as(GetDeviceAlarm200Response.class));
    }
}
