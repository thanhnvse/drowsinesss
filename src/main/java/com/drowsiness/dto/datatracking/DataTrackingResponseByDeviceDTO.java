package com.drowsiness.dto.datatracking;

import com.drowsiness.dto.device.DeviceResponseForDataTrackingByDeviceDTO;
import lombok.Data;

import java.util.List;

@Data
public class DataTrackingResponseByDeviceDTO {
    private DeviceResponseForDataTrackingByDeviceDTO deviceDTO;
    private List<DataTrackingResponseWithDevice> dataTrackings;

    public DataTrackingResponseByDeviceDTO(DeviceResponseForDataTrackingByDeviceDTO deviceDTO, List<DataTrackingResponseWithDevice> dataTrackings) {
        this.deviceDTO = deviceDTO;
        this.dataTrackings = dataTrackings;
    }
}
