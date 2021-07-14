package com.drowsiness.dto.device;

import com.drowsiness.model.Firmware;
import lombok.Data;

import java.io.Serializable;
import java.util.UUID;

@Data
public class DeviceResponseDTO implements Serializable {

    private UUID deviceId;

    private String deviceName;

    private Long createdAt;

    private Long updatedAt;

    private Firmware firmware;
}
