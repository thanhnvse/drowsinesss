package com.drowsiness.service;

import com.drowsiness.model.Firmware;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface FirmwareService {
    List<Firmware> findAllFirmware();

    Optional<Firmware> findFirmwareById(UUID id);

    Firmware saveFirmware(Firmware firmware);

    void removeFirmware(Firmware firmware);

    Firmware findNewestFirmware();
}
