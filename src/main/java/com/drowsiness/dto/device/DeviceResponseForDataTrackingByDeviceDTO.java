package com.drowsiness.dto.device;

import lombok.Data;

import java.util.UUID;

@Data
public class DeviceResponseForDataTrackingByDeviceDTO {
    private UUID deviceId;

    private String deviceName;

    private Long createdAt;

    private Long updatedAt;
}
