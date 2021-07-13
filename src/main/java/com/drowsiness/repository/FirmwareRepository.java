package com.drowsiness.repository;

import com.drowsiness.model.Firmware;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface FirmwareRepository extends JpaRepository<Firmware, UUID> {
    @Query("SELECT f FROM Firmware f WHERE f.createdAt = (SELECT MAX (createdAt) FROM Firmware )")
    Firmware findNewestFirmware();
}
