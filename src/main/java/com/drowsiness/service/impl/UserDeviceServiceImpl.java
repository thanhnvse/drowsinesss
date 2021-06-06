package com.drowsiness.service.impl;

import com.drowsiness.model.Device;
import com.drowsiness.model.User;
import com.drowsiness.model.UserDevice;
import com.drowsiness.repository.UserDeviceRepository;
import com.drowsiness.service.UserDeviceService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class UserDeviceServiceImpl implements UserDeviceService {
    private UserDeviceRepository userDeviceRepository;

    @Autowired
    public void setUserDeviceRepository(UserDeviceRepository userDeviceRepository) {
        this.userDeviceRepository = userDeviceRepository;
    }

    @Override
    public List<UserDevice> findAllUserDevice() {
        return userDeviceRepository.findAll();
    }

    @Override
    public User findUserByUserDeviceConnected(String userId) {
        return userDeviceRepository.findByUserIdAndConnected(userId);
    }

    @Override
    public UserDevice findByUserIdAndDeviceId(String userId, String deviceId) {
        return userDeviceRepository.findByUserIdAndDeviceId(userId,deviceId);
    }

    @Override
    public UserDevice findDeviceByUserDeviceConnected(String deviceId) {
        return userDeviceRepository.findByDeviceIdAndAndConnected(deviceId);
    }

    @Override
    public Optional<UserDevice> findUserDeviceById(String id) {
        return userDeviceRepository.findById(id);
    }

    @Override
    public UserDevice findUserDeviceByUserDeviceId(String id) {
        return userDeviceRepository.findById(id).get();
    }

    @Override
    public UserDevice saveUserDevice(UserDevice userDevice) {
        return userDeviceRepository.save(userDevice);
    }

    @Override
    public void removeUserDevice(UserDevice userDevice) {
        userDeviceRepository.delete(userDevice);
    }
}
