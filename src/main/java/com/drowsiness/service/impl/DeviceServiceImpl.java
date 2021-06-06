package com.drowsiness.service.impl;

import com.drowsiness.model.Device;
import com.drowsiness.repository.DeviceRepository;
import com.drowsiness.service.DeviceService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class DeviceServiceImpl implements DeviceService {
    private DeviceRepository deviceRepository;

    @Autowired
    public DeviceServiceImpl(DeviceRepository deviceRepository) {
        this.deviceRepository = deviceRepository;
    }

    @Override
    public List<Device> findAllDevice() {
        return deviceRepository.findAll();
    }

    @Override
    public Optional<Device> findDeviceById(String id) {
        return deviceRepository.findById(id);
    }

    @Override
    public Device findDeviceByDeviceId(String id) {
        return deviceRepository.findById(id).get();
    }

    @Override
    public Device saveDevice(Device device) {
        return deviceRepository.save(device);
    }

    @Override
    public void removeDevice(Device device) {
        deviceRepository.delete(device);
    }
}