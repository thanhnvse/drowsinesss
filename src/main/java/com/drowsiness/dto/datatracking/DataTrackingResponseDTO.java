package com.drowsiness.dto.datatracking;

import com.drowsiness.model.Device;
import com.drowsiness.model.User;
import lombok.Data;

import java.io.Serializable;

@Data
public class DataTrackingResponseDTO implements Serializable {
    private User user;
    private Device device;
}
