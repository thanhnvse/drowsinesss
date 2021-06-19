package com.drowsiness.service.impl;

import com.drowsiness.model.User;
import com.drowsiness.model.UserDevice;
import com.drowsiness.repository.UserDeviceRepository;
import com.drowsiness.service.UserDeviceService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
public class UserDeviceServiceImpl implements UserDeviceService{
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
    public User findUserByUserDeviceConnected(UUID userId) {
        return userDeviceRepository.findByUserIdAndConnected(userId);
    }

    @Override
    public UserDevice findByUserIdAndDeviceId(UUID userId, UUID deviceId) {
        return userDeviceRepository.findByUserIdAndDeviceId(userId,deviceId);
    }

    @Override
    public boolean checkExistedUser(UUID userId, UUID deviceId) {
        return userDeviceRepository.existsByUserDeviceId(userId,deviceId);
    }

    @Override
    public UserDevice findDeviceByUserDeviceConnected(UUID deviceId) {
        return userDeviceRepository.findByDeviceIdAndAndConnected(deviceId);
    }

    @Override
    public Optional<UserDevice> findUserDeviceById(UUID id) {
        return userDeviceRepository.findById(id);
    }

    @Override
    public List<User> findUserByDeviceId(UUID deviceId) {
        return userDeviceRepository.findUserByDeviceId(deviceId);
    }

    @Override
    public UserDevice findUserDeviceByUserDeviceId(UUID id) {
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

    @Override
    public List<UserDevice> findUserDeviceByDeviceId(UUID deviceId) {
        return userDeviceRepository.findUserDeviceByDeviceId(deviceId);
    }

    @Override
    public List<UserDevice> findAllDeviceConnectedByUserId(UUID userId) {
        return userDeviceRepository.findAllDeviceConnectedByUserId(userId);
    }
}
