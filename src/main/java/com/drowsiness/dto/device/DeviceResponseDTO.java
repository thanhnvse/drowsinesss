package com.drowsiness.dto.device;

import lombok.Data;

import java.io.Serializable;

@Data
public class DeviceResponseDTO implements Serializable {

    private String deviceName;

    private Long createdAt;

    private Long updatedAt;
}
