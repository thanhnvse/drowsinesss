package com.drowsiness.repository;

import com.drowsiness.model.Device;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface DeviceRepository extends JpaRepository<Device, UUID> {
    @Query("SELECT d FROM Device d WHERE d.deviceId in " +
            "(SELECT u.device.deviceId FROM UserDevice u WHERE u.accountUser.userId = :userId)")
    List<Device> findByUseId(@Param("userId") UUID userId);

    @Query("SELECT COUNT(1) FROM Device WHERE serialId = :serialId")
    int checkDuplicateSerialID(@Param("serialId") String serialId);

    @Query("SELECT COUNT(1) FROM Device WHERE serialId = :serialId AND deviceId <> :deviceID")
    int checkDuplicateSerialID(@Param("serialId") String serialId,@Param("deviceID") UUID deviceID);
}

