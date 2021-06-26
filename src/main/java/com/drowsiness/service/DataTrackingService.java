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
    List<DataTracking> findByUserDeviceIdFromUserIdAndDeviceIdConnected(UUID userId, UUID deviceId);
    List<DataTracking> findByUserDeviceIdFromUserIdAndDeviceId(UUID userId, UUID deviceId);
    List<DataTracking> findByUserDeviceIdFromUserId(UUID userId);
}
