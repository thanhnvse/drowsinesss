package com.drowsiness.repository;

import com.drowsiness.model.User;
import com.drowsiness.model.UserDevice;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface UserDeviceRepository extends JpaRepository<UserDevice, UUID> {
    @Query("SELECT u FROM User u WHERE u.userId = (SELECT ud.accountUser.userId FROM UserDevice ud WHERE ud.isConnected = true " +
            "AND ud.accountUser.userId = :userId)")
    User findByUserIdAndConnected(@Param("userId") UUID userId);

    @Query("SELECT ud FROM UserDevice ud WHERE ud.accountUser.userId = :userId AND ud.device.deviceId = :deviceId")
    UserDevice findByUserIdAndDeviceId(@Param("userId") UUID userId, @Param("deviceId") UUID deviceId);

    @Query("SELECT ud FROM UserDevice ud WHERE ud.isConnected = true AND ud.device.deviceId = :deviceId")
    UserDevice findByDeviceIdAndAndConnected(@Param("deviceId") UUID deviceId);

    @Query("SELECT case WHEN (count(ud) > 0)  then true else false end FROM UserDevice ud WHERE ud.accountUser.userId = :userId AND ud.device.deviceId = :deviceId")
    boolean existsByUserDeviceId(@Param("userId") UUID userId, @Param("deviceId") UUID deviceId);
}
