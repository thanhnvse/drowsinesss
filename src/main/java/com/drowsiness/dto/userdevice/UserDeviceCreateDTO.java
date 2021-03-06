package com.drowsiness.dto.userdevice;

import lombok.Data;

import java.io.Serializable;
import java.util.UUID;

@Data
public class UserDeviceCreateDTO implements Serializable {
    private UUID userId;

    private UUID deviceId;
}
