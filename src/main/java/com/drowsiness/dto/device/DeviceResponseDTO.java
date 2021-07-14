package com.drowsiness.dto.device;

import com.drowsiness.model.Firmware;
import lombok.Data;

import java.io.Serializable;

@Data
public class DeviceResponseDTO implements Serializable {

    private String deviceName;

    private Long createdAt;

    private Long updatedAt;

    private Firmware firmware;
}
