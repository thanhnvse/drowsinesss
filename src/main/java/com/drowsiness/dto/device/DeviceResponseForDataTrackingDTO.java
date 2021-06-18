package com.drowsiness.dto.device;

import com.drowsiness.dto.user.UserResponseForDataTrackingDTO;
import lombok.Data;

import java.util.List;
import java.util.UUID;

@Data
public class DeviceResponseForDataTrackingDTO {
    private UUID deviceId;

    private String deviceName;

    private Long createdAt;

    private Long updatedAt;

    private List<UserResponseForDataTrackingDTO> users;
}
