package com.drowsiness.repository;

import com.drowsiness.model.Firmware;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

@Repository
public interface FirmwareRepository extends JpaRepository<Firmware, UUID> {
    @Query("SELECT f FROM Firmware f WHERE f.isActive = true")
    Firmware findNewestFirmware();

    @Transactional
    @Modifying
    @Query("UPDATE Firmware SET isActive = FALSE WHERE firmwareId <> :firmwareId")
    void deactivateFirmwaresExceptThis(@Param("firmwareId") UUID firmwareId);
}
