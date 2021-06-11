package com.drowsiness.service.impl;

import com.drowsiness.model.DataTracking;
import com.drowsiness.repository.DataTrackingRepository;
import com.drowsiness.service.DataTrackingService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
public class DataTrackingServiceImpl implements DataTrackingService {
    private DataTrackingRepository dataTrackingRepository;

    @Autowired
    public void setDataTrackingRepository(DataTrackingRepository dataTrackingRepository) {
        this.dataTrackingRepository = dataTrackingRepository;
    }

    @Override
    public List<DataTracking> findAllDataTracking() {
        return dataTrackingRepository.findAll();
    }

    @Override
    public List<DataTracking> findAllDataTrackingNotDeleted() {
        return dataTrackingRepository.findAllByNotDeleted();
    }

    @Override
    public Optional<DataTracking> findDataTrackingById(UUID id) {
        return dataTrackingRepository.findById(id);
    }

    @Override
    public DataTracking findDataTrackingByDataTrackingId(UUID id) {
        return dataTrackingRepository.findById(id).get();
    }

    @Override
    public DataTracking saveDataTracking(DataTracking dataTracking) {
        return dataTrackingRepository.save(dataTracking);
    }

    @Override
    public List<DataTracking> findByUserDeviceIdFromUserIdAndDeviceId(UUID userId, UUID deviceId) {
        return dataTrackingRepository.findByUserDeviceIdFromUserIdAndDeviceId(userId,deviceId);
    }
}
