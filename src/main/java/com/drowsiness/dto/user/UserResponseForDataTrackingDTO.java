package com.drowsiness.dto.user;

import com.drowsiness.model.DataTracking;
import lombok.Data;

import java.util.List;
import java.util.UUID;

@Data
public class UserResponseForDataTrackingDTO {
    private UUID userId;

    private String username;

    private String fullName;

    private String phoneNumber;

    private String email;

    private String avatar;

    private boolean isActive;

    private Long createdAt;

    private Long updatedAt;

    private List<DataTracking> dataTrackings;
}
