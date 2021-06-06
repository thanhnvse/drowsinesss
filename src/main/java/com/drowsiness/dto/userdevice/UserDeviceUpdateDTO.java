package com.drowsiness.dto.userdevice;

import lombok.Data;

import java.io.Serializable;

@Data
public class UserDeviceUpdateDTO implements Serializable {
    private boolean isConnected;
}
