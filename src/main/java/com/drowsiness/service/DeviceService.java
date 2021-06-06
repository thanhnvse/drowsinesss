package com.drowsiness.service;

import com.drowsiness.model.Device;

import java.util.List;
import java.util.Optional;

public interface DeviceService {
    List<Device> findAllDevice();
    Optional<Device> findDeviceById(String id);
    Device findDeviceByDeviceId(String id);
    Device saveDevice(Device device);
    void removeDevice(Device device);
}
