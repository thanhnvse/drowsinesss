package com.drowsiness.service.impl;


import com.drowsiness.model.Firmware;
import com.drowsiness.repository.FirmwareRepository;
import com.drowsiness.service.FirmwareService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
public class FirmwareServiceImpl implements FirmwareService {
    private FirmwareRepository fwRepo;

    @Autowired
    public void setFirmwareRepository(FirmwareRepository fwRepo) {
        this.fwRepo = fwRepo;
    }

    public List<Firmware> findAllFirmware() {
        return this.fwRepo.findAll();
    }

    public Optional<Firmware> findFirmwareById(UUID id) {
        return this.fwRepo.findById(id);
    }

    public Firmware saveFirmware(Firmware firmware) {
        return (Firmware)this.fwRepo.save(firmware);
    }

    public void removeFirmware(Firmware firmware) {
        this.fwRepo.delete(firmware);
    }

    public Firmware findNewestFirmware() {
        return this.fwRepo.findNewestFirmware();
    }
}
