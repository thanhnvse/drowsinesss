package com.drowsiness.dto.userdevice;

import com.drowsiness.model.Device;
import com.drowsiness.model.User;
import lombok.Data;

import java.io.Serializable;
import java.util.UUID;

@Data
public class UserDeviceConnectedResponseDTO implements Serializable {
    private UUID userDeviceId;

    private User accountUser;

    private Device device;

    private boolean isConnected;

    private Long connectedAt;

    private Long updatedAt;

    private Long disconnectedAt;
}
