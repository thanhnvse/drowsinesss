package com.drowsiness.dto.datatracking;

import lombok.Data;

import java.io.Serializable;

@Data
public class DataTrackingUpdateDTO implements Serializable {
    private boolean isDeleted;
}
