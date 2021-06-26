package com.drowsiness.dto.datatracking;

import lombok.Data;

import java.util.UUID;

@Data
public class DataTrackingCreateImageDTO {
    private UUID userId;
    private UUID deviceId;
}
