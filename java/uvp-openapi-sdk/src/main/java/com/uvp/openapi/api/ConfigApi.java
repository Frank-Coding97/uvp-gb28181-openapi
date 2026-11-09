// GENERATED FILE - DO NOT EDIT.
// Source: contract/openapi-v1.yaml via java/codegen/generate_apis.py
package com.uvp.openapi.api;

import com.uvp.openapi.internal.ApiInvoker;
import com.uvp.openapi.internal.Call;
import com.uvp.openapi.model.*;

/**
 * Device configuration, video parameters and firmware upgrade tasks.
 *
 * <p>Signing, retries and error mapping happen inside {@link ApiInvoker}; no method here takes a
 * signature argument.</p>
 */
public final class ConfigApi {

    private final ApiInvoker invoker;

    public ConfigApi(ApiInvoker invoker) {
        this.invoker = invoker;
    }

    /**
     * Query supported device configuration blocks
     *
     * <p>{@code POST /openapi/v1/config/refresh}</p>
     *
     * <p>Required scope: {@code device:config:refresh}</p>
     * <p>The platform requires an {@code Idempotency-Key} for this operation. One is
     * generated automatically; pin it with {@link com.uvp.openapi.client.UvpIdempotency}
     * when you retry after an unknown outcome.</p>
     */
    public DeviceConfigOperationResponse deviceConfigRefresh(ActionDeviceConfigRefreshRequest body) {
        return invoker.execute(Call.post("/openapi/v1/config/refresh")
            .body(body)
            .idempotency(Call.Idempotency.REQUIRED)
            .slow()
            .as(DeviceConfigOperationResponse.class));
    }

    /**
     * Read the latest stored device configuration snapshot
     *
     * <p>{@code GET /openapi/v1/config/info}</p>
     *
     * <p>Required scope: {@code device:config:read}</p>
     */
    public DeviceConfigSnapshotResponse deviceConfigRead(String deviceId, String channelId) {
        return invoker.execute(Call.get("/openapi/v1/config/info")
            .query("deviceId", deviceId)
            .query("channelId", channelId)
            .as(DeviceConfigSnapshotResponse.class));
    }

    /**
     * Apply validated configuration blocks to a device
     *
     * <p>{@code POST /openapi/v1/config/set}</p>
     *
     * <p>Required scope: {@code device:config:apply}</p>
     * <p>The platform requires an {@code Idempotency-Key} for this operation. One is
     * generated automatically; pin it with {@link com.uvp.openapi.client.UvpIdempotency}
     * when you retry after an unknown outcome.</p>
     */
    public DeviceConfigOperationResponse deviceConfigApply(ActionDeviceConfigApplyRequest body) {
        return invoker.execute(Call.post("/openapi/v1/config/set")
            .body(body)
            .idempotency(Call.Idempotency.REQUIRED)
            .slow()
            .as(DeviceConfigOperationResponse.class));
    }

    /**
     * Query GB/T 28181-2022 video stream parameters
     *
     * <p>{@code POST /openapi/v1/video/params/refresh}</p>
     *
     * <p>Required scope: {@code channel:video-params:refresh}</p>
     * <p>The platform requires an {@code Idempotency-Key} for this operation. One is
     * generated automatically; pin it with {@link com.uvp.openapi.client.UvpIdempotency}
     * when you retry after an unknown outcome.</p>
     */
    public DeviceConfigOperationResponse videoParamsRefresh(ActionVideoParamsRefreshRequest body) {
        return invoker.execute(Call.post("/openapi/v1/video/params/refresh")
            .body(body)
            .idempotency(Call.Idempotency.REQUIRED)
            .slow()
            .as(DeviceConfigOperationResponse.class));
    }

    /**
     * Read the latest stored GB/T 28181-2022 video parameters
     *
     * <p>{@code GET /openapi/v1/video/params/info}</p>
     *
     * <p>Required scope: {@code channel:video-params:read}</p>
     */
    public VideoParamsSnapshotResponse videoParamsRead(String deviceId, String channelId) {
        return invoker.execute(Call.get("/openapi/v1/video/params/info")
            .query("deviceId", deviceId)
            .query("channelId", channelId)
            .as(VideoParamsSnapshotResponse.class));
    }

    /**
     * Apply GB/T 28181-2022 video stream parameters
     *
     * <p>{@code POST /openapi/v1/video/params/set}</p>
     *
     * <p>Required scope: {@code channel:video-params:apply}</p>
     * <p>The platform requires an {@code Idempotency-Key} for this operation. One is
     * generated automatically; pin it with {@link com.uvp.openapi.client.UvpIdempotency}
     * when you retry after an unknown outcome.</p>
     */
    public DeviceConfigOperationResponse videoParamsApply(ActionVideoParamsApplyRequest body) {
        return invoker.execute(Call.post("/openapi/v1/video/params/set")
            .body(body)
            .idempotency(Call.Idempotency.REQUIRED)
            .slow()
            .as(DeviceConfigOperationResponse.class));
    }

    /**
     * Read a configuration operation owned by the authenticated client
     *
     * <p>{@code GET /openapi/v1/config/operation/info}</p>
     *
     * <p>Required scope: {@code device:config:operation:read}</p>
     */
    public DeviceConfigOperationResponse deviceConfigOperationRead(String deviceId, String channelId, String operationId) {
        return invoker.execute(Call.get("/openapi/v1/config/operation/info")
            .query("deviceId", deviceId)
            .query("channelId", channelId)
            .query("operationId", operationId)
            .as(DeviceConfigOperationResponse.class));
    }

    /**
     * 设备固件升级
     *
     * <p>{@code POST /openapi/v1/firmware/upgrade}</p>
     *
     * <p>Required scope: {@code device:firmware-upgrade:execute}</p>
     * <p>The platform requires an {@code Idempotency-Key} for this operation. One is
     * generated automatically; pin it with {@link com.uvp.openapi.client.UvpIdempotency}
     * when you retry after an unknown outcome.</p>
     */
    public OpenAPIDeviceFirmwareUpgradeResponse deviceFirmwareUpgradeCreate(ActionDeviceFirmwareUpgradeCreateRequest body) {
        return invoker.execute(Call.post("/openapi/v1/firmware/upgrade")
            .body(body)
            .idempotency(Call.Idempotency.REQUIRED)
            .slow()
            .as(OpenAPIDeviceFirmwareUpgradeResponse.class));
    }

    /**
     * 设备固件升级历史
     *
     * <p>{@code GET /openapi/v1/firmware/list}</p>
     *
     * <p>Required scope: {@code device:firmware-upgrade:list}</p>
     */
    public OpenAPIFirmwareUpgradeHistoryResponse deviceFirmwareUpgradeList(String deviceId) {
        return invoker.execute(Call.get("/openapi/v1/firmware/list")
            .query("deviceId", deviceId)
            .as(OpenAPIFirmwareUpgradeHistoryResponse.class));
    }

    /**
     * 设备固件升级结果
     *
     * <p>{@code GET /openapi/v1/firmware/info}</p>
     *
     * <p>Required scope: {@code device:firmware-upgrade:read}</p>
     */
    public OpenAPIDeviceFirmwareUpgradeResponse deviceFirmwareUpgradeRead(String deviceId, String operationId) {
        return invoker.execute(Call.get("/openapi/v1/firmware/info")
            .query("deviceId", deviceId)
            .query("operationId", operationId)
            .as(OpenAPIDeviceFirmwareUpgradeResponse.class));
    }
}
