package com.drowsiness.dto.datatracking;

import lombok.Data;

import java.io.Serializable;
import java.util.UUID;

@Data
public class DataTrackingCreateDTO implements Serializable {
    private UUID userId;
    private UUID deviceId;
}
