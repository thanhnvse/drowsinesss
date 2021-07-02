package com.drowsiness.service;

import com.drowsiness.model.User;
import com.drowsiness.model.UserDevice;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface UserDeviceService {
    List<UserDevice> findAllUserDevice();
    User findUserByUserDeviceConnected(UUID userId);
    UserDevice findByUserIdAndDeviceId(UUID userId, UUID deviceId);
    boolean checkExistedUser(UUID userId, UUID deviceId);
    UserDevice findDeviceByUserDeviceConnected(UUID deviceId);
    Optional<UserDevice> findUserDeviceById(UUID id);
    List<User> findUserByDeviceId(UUID deviceId);
    UserDevice findUserDeviceByUserDeviceId(UUID id);
    UserDevice saveUserDevice(UserDevice userDevice);
    void removeUserDevice(UserDevice userDevice);
    List<UserDevice> findUserDeviceByDeviceId(UUID deviceId);
    List<UserDevice> findAllDeviceConnectedByUserId(UUID userId);
    UserDevice findByUserIdAndAndConnected(UUID userId);
}
