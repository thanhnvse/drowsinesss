package com.drowsiness.dto.datatracking;

import lombok.Data;
import org.springframework.web.multipart.MultipartFile;

import java.util.UUID;

@Data
public class DataTrackingCreateImageDTO {
    private UUID userId;
    private UUID deviceId;
    private Long trackingAt;
    private MultipartFile file;
}
