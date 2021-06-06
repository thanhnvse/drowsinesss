package com.drowsiness.dto.userdevice;

import lombok.Data;

import java.io.Serializable;

@Data
public class UserDeviceCreateDTO implements Serializable {
    private String userId;

    private String deviceId;
}
