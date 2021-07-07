package com.drowsiness.dto.datatracking;

import lombok.Data;

import java.util.UUID;

@Data
public class DataTrackingFullDTO {
    private UUID dataTrackingId;

    private Long trackingAt;

    private boolean isDeleted;

    private String imageUrl;

    private UUID userDeviceId;

    private UUID userId;

    private UUID deviceId;
}
