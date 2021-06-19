package com.drowsiness.repository;

import com.drowsiness.model.Device;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface DeviceRepository extends JpaRepository<Device, UUID> {

}
