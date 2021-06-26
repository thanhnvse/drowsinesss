package com.drowsiness.service;

import com.drowsiness.model.Device;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface DeviceService {
    List<Device> findAllDevice();
    Optional<Device> findDeviceById(UUID id);
    Device findDeviceByDeviceId(UUID id);
    Device saveDevice(Device device);
    List<Device> findAllDeviceByUserId(UUID userId);
    void removeDevice(Device device);
}
