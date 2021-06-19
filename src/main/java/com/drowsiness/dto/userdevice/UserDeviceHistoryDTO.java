package com.drowsiness.dto.userdevice;

import lombok.Data;

import java.io.Serializable;
import java.util.UUID;

@Data
public class UserDeviceHistoryDTO implements Serializable {
    private UUID deviceId;

    private String deviceName;

    private Long createdAt;

    private boolean isConnected;

    private Long connectedAt;

    private Long disconnectedAt;
}
