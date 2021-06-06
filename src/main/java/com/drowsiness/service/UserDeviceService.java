package com.drowsiness.service;

import com.drowsiness.model.Device;
import com.drowsiness.model.User;
import com.drowsiness.model.UserDevice;

import java.util.List;
import java.util.Optional;

public interface UserDeviceService {
    List<UserDevice> findAllUserDevice();
    User findUserByUserDeviceConnected(String userId);
    UserDevice findByUserIdAndDeviceId(String userId, String deviceId);
    UserDevice findDeviceByUserDeviceConnected(String deviceId);
    Optional<UserDevice> findUserDeviceById(String id);
    UserDevice findUserDeviceByUserDeviceId(String id);
    UserDevice saveUserDevice(UserDevice userDevice);
    void removeUserDevice(UserDevice userDevice);
}
