package com.drowsiness.service;

import com.drowsiness.model.DataTracking;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface DataTrackingService {
    List<DataTracking> findAllDataTracking();
    List<DataTracking> findAllDataTrackingNotDeleted();
    Optional<DataTracking> findDataTrackingById(UUID id);
    DataTracking findDataTrackingByDataTrackingId(UUID id);
    DataTracking saveDataTracking(DataTracking dataTracking);
    DataTracking findByUserDeviceId(UUID userId, UUID deviceId);
}
