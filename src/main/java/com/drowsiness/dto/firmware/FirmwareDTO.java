package com.drowsiness.dto.firmware;

import lombok.Data;
import org.springframework.web.multipart.MultipartFile;

@Data
public class FirmwareDTO {
    private String description;
    private float timeDetection;
    private MultipartFile file;
}
