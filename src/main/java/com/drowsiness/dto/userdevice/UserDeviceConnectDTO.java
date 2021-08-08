package com.drowsiness.dto.userdevice;

import com.drowsiness.model.Device;
import com.drowsiness.model.User;
import lombok.Data;

import java.io.Serializable;
import java.util.UUID;

@Data
public class UserDeviceConnectDTO implements Serializable {

    private UUID userDeviceId;

    private boolean isConnected;

    private Long connectedAt;

    private Long updatedAt;

    private Long disconnectedAt;


    private UUID userId;
    private String username;
    private String fullName;
    private String phoneNumber;
    private String email;
    private String avatar;

}
