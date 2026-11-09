// GENERATED FILE - DO NOT EDIT.
// Source: contract/openapi-v1.yaml via java/codegen/generate_apis.py
package com.uvp.openapi.api;

import com.uvp.openapi.internal.ApiInvoker;
import com.uvp.openapi.internal.Call;
import com.uvp.openapi.model.*;

/**
 * Recording control, alarm guard, reboot, storage card format, task polling.
 *
 * <p>Signing, retries and error mapping happen inside {@link ApiInvoker}; no method here takes a
 * signature argument.</p>
 */
public final class ControlApi {

    private final ApiInvoker invoker;

    public ControlApi(ApiInvoker invoker) {
        this.invoker = invoker;
    }

    /**
     * Read cached storage card status for one owned channel
     *
     * <p>{@code GET /openapi/v1/storage/card/list}</p>
     *
     * <p>Required scope: {@code channel:storage-cards}</p>
     */
    public StorageCardSnapshotResponse getChannelStorageCards(String deviceId, String channelId) {
        return invoker.execute(Call.get("/openapi/v1/storage/card/list")
            .query("deviceId", deviceId)
            .query("channelId", channelId)
            .as(StorageCardSnapshotResponse.class));
    }

    /**
     * 启动设备录像
     *
     * <p>{@code POST /openapi/v1/record/start}</p>
     *
     * <p>Required scope: {@code device:recording:start}</p>
     * <p>The platform requires an {@code Idempotency-Key} for this operation. One is
     * generated automatically; pin it with {@link com.uvp.openapi.client.UvpIdempotency}
     * when you retry after an unknown outcome.</p>
     */
    public OpenAPIChannelRecordingStartResponse channelRecordingStart(ActionChannelRecordingStartRequest body) {
        return invoker.execute(Call.post("/openapi/v1/record/start")
            .body(body)
            .idempotency(Call.Idempotency.REQUIRED)
            .slow()
            .as(OpenAPIChannelRecordingStartResponse.class));
    }

    /**
     * 停止设备录像
     *
     * <p>{@code POST /openapi/v1/record/stop}</p>
     *
     * <p>Required scope: {@code device:recording:stop}</p>
     * <p>The platform requires an {@code Idempotency-Key} for this operation. One is
     * generated automatically; pin it with {@link com.uvp.openapi.client.UvpIdempotency}
     * when you retry after an unknown outcome.</p>
     */
    public OpenAPIChannelRecordingStopResponse channelRecordingStop(ActionChannelRecordingStopRequest body) {
        return invoker.execute(Call.post("/openapi/v1/record/stop")
            .body(body)
            .idempotency(Call.Idempotency.REQUIRED)
            .slow()
            .as(OpenAPIChannelRecordingStopResponse.class));
    }

    /**
     * 设备报警布防
     *
     * <p>{@code POST /openapi/v1/alarm/guard/set}</p>
     *
     * <p>Required scope: {@code device:alarm:guard:set}</p>
     * <p>The platform requires an {@code Idempotency-Key} for this operation. One is
     * generated automatically; pin it with {@link com.uvp.openapi.client.UvpIdempotency}
     * when you retry after an unknown outcome.</p>
     */
    public OpenAPIChannelAlarmGuardSetResponse channelAlarmGuardSet(ActionChannelAlarmGuardSetRequest body) {
        return invoker.execute(Call.post("/openapi/v1/alarm/guard/set")
            .body(body)
            .idempotency(Call.Idempotency.REQUIRED)
            .slow()
            .as(OpenAPIChannelAlarmGuardSetResponse.class));
    }

    /**
     * 设备报警撤防
     *
     * <p>{@code POST /openapi/v1/alarm/guard/reset}</p>
     *
     * <p>Required scope: {@code device:alarm:guard:reset}</p>
     * <p>The platform requires an {@code Idempotency-Key} for this operation. One is
     * generated automatically; pin it with {@link com.uvp.openapi.client.UvpIdempotency}
     * when you retry after an unknown outcome.</p>
     */
    public OpenAPIChannelAlarmGuardResetResponse channelAlarmGuardReset(ActionChannelAlarmGuardResetRequest body) {
        return invoker.execute(Call.post("/openapi/v1/alarm/guard/reset")
            .body(body)
            .idempotency(Call.Idempotency.REQUIRED)
            .slow()
            .as(OpenAPIChannelAlarmGuardResetResponse.class));
    }

    /**
     * 设备报警复位
     *
     * <p>{@code POST /openapi/v1/alarm/reset}</p>
     *
     * <p>Required scope: {@code device:alarm:reset}</p>
     * <p>The platform requires an {@code Idempotency-Key} for this operation. One is
     * generated automatically; pin it with {@link com.uvp.openapi.client.UvpIdempotency}
     * when you retry after an unknown outcome.</p>
     */
    public OpenAPIChannelAlarmResetResponse channelAlarmReset(ActionChannelAlarmResetRequest body) {
        return invoker.execute(Call.post("/openapi/v1/alarm/reset")
            .body(body)
            .idempotency(Call.Idempotency.REQUIRED)
            .slow()
            .as(OpenAPIChannelAlarmResetResponse.class));
    }

    /**
     * 设备重启
     *
     * <p>{@code POST /openapi/v1/device/reboot}</p>
     *
     * <p>Required scope: {@code device:maintenance:reboot}</p>
     * <p>The platform requires an {@code Idempotency-Key} for this operation. One is
     * generated automatically; pin it with {@link com.uvp.openapi.client.UvpIdempotency}
     * when you retry after an unknown outcome.</p>
     */
    public OpenAPIDeviceMaintenanceRebootResponse deviceMaintenanceReboot(ActionDeviceMaintenanceRebootRequest body) {
        return invoker.execute(Call.post("/openapi/v1/device/reboot")
            .body(body)
            .idempotency(Call.Idempotency.REQUIRED)
            .slow()
            .as(OpenAPIDeviceMaintenanceRebootResponse.class));
    }

    /**
     * 存储卡格式化
     *
     * <p>{@code POST /openapi/v1/storage/card/format}</p>
     *
     * <p>Required scope: {@code channel:storage-card:format}</p>
     * <p>The platform requires an {@code Idempotency-Key} for this operation. One is
     * generated automatically; pin it with {@link com.uvp.openapi.client.UvpIdempotency}
     * when you retry after an unknown outcome.</p>
     */
    public OpenAPIChannelStorageCardFormatResponse channelStorageCardFormat(ActionChannelStorageCardFormatRequest body) {
        return invoker.execute(Call.post("/openapi/v1/storage/card/format")
            .body(body)
            .idempotency(Call.Idempotency.REQUIRED)
            .slow()
            .as(OpenAPIChannelStorageCardFormatResponse.class));
    }

    /**
     * 设备维护操作记录
     *
     * <p>{@code GET /openapi/v1/maintenance/list}</p>
     *
     * <p>Required scope: {@code device:maintenance:operation:list}</p>
     */
    public OpenAPIDeviceMaintenanceOperationListResponse deviceMaintenanceOperationList(String deviceId, Integer page, Integer pageSize) {
        return invoker.execute(Call.get("/openapi/v1/maintenance/list")
            .query("deviceId", deviceId)
            .query("page", page)
            .query("pageSize", pageSize)
            .as(OpenAPIDeviceMaintenanceOperationListResponse.class));
    }

    /** Same as {@link #deviceMaintenanceOperationList} with optional arguments left unset. */
    public OpenAPIDeviceMaintenanceOperationListResponse deviceMaintenanceOperationList(String deviceId) {
        return deviceMaintenanceOperationList(deviceId, null, null);
    }

    /**
     * 维护操作状态
     *
     * <p>{@code GET /openapi/v1/maintenance/info}</p>
     *
     * <p>Required scope: {@code device:maintenance:operation:read}</p>
     */
    public OpenAPIDeviceMaintenanceOperationReadResponse deviceMaintenanceOperationRead(String deviceId, String operationId) {
        return invoker.execute(Call.get("/openapi/v1/maintenance/info")
            .query("deviceId", deviceId)
            .query("operationId", operationId)
            .as(OpenAPIDeviceMaintenanceOperationReadResponse.class));
    }

    /**
     * 主动查询设备存储卡
     *
     * <p>{@code POST /openapi/v1/storage/card/refresh}</p>
     *
     * <p>Required scope: {@code channel:storage-card:refresh}</p>
     * <p>The platform requires an {@code Idempotency-Key} for this operation. One is
     * generated automatically; pin it with {@link com.uvp.openapi.client.UvpIdempotency}
     * when you retry after an unknown outcome.</p>
     */
    public RefreshChannelStorage200Response refreshChannelStorage(ActionRefreshChannelStorageRequest body) {
        return invoker.execute(Call.post("/openapi/v1/storage/card/refresh")
            .body(body)
            .idempotency(Call.Idempotency.REQUIRED)
            .slow()
            .as(RefreshChannelStorage200Response.class));
    }
}
