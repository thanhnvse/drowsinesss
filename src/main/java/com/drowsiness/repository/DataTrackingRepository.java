package com.drowsiness.repository;

import com.drowsiness.model.DataTracking;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface DataTrackingRepository extends JpaRepository<DataTracking, UUID> {
    @Query("SELECT d FROM DataTracking d WHERE d.isDeleted = false")
    List<DataTracking> findAllByNotDeleted();

    @Query("SELECT d FROM DataTracking d WHERE d.userDevice.userDeviceId = " +
            "(SELECT u.userDeviceId FROM UserDevice u WHERE u.accountUser.userId = :userId AND u.device.deviceId = :deviceId " +
            "AND u.isConnected = true)")
    List<DataTracking> findByUserDeviceIdFromUserIdAndDeviceIdConnected(@Param("userId") UUID userId, @Param("deviceId") UUID deviceId);

    @Query("SELECT d FROM DataTracking d WHERE d.userDevice.userDeviceId = " +
            "(SELECT u.userDeviceId FROM UserDevice u WHERE u.accountUser.userId = :userId AND u.device.deviceId = :deviceId)")
    List<DataTracking> findByUserDeviceIdFromUserIdAndDeviceId(@Param("userId") UUID userId, @Param("deviceId") UUID deviceId);

    @Query("SELECT d FROM DataTracking d WHERE d.userDevice.userDeviceId = " +
            "(SELECT u.userDeviceId FROM UserDevice u WHERE u.accountUser.userId = :userId)")
    List<DataTracking> findByUserDeviceIdFromUserId(@Param("userId") UUID userId);
}
