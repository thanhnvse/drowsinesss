package com.drowsiness.repository;

import com.drowsiness.model.User;
import com.drowsiness.model.UserDevice;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface UserDeviceRepository extends JpaRepository<UserDevice, String> {
    @Query("SELECT u FROM User u WHERE u.userId = (SELECT ud.user.userId FROM UserDevice ud WHERE ud.isConnected = true " +
            "AND ud.user.userId = :userId)")
    User findByUserIdAndConnected(@Param("userId") String userId);

    @Query("SELECT ud FROM UserDevice ud WHERE ud.user.userId = :userId AND ud.device.deviceId = :deviceId")
    UserDevice findByUserIdAndDeviceId(@Param("userId") String userId, @Param("deviceId") String deviceId);

    @Query("SELECT ud FROM UserDevice ud WHERE ud.isConnected = true AND ud.device.deviceId = :deviceId")
    UserDevice findByDeviceIdAndAndConnected(@Param("deviceId") String deviceId);
}
