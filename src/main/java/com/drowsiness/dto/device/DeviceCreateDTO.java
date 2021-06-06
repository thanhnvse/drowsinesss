package com.drowsiness.dto.device;

import lombok.Data;

import java.io.Serializable;

@Data
public class DeviceCreateDTO implements Serializable {
    private String deviceName;
}
