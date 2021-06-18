package com.drowsiness.dto.datatracking;

import lombok.Data;

import java.util.UUID;

@Data
public class DataTrackingResponseForUserAndDeviceDTO {
    private UUID dataTrackingId;

    private Long trackingAt;

    private boolean isDeleted;

    private String imageUrl;
}
