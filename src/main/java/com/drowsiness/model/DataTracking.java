package com.drowsiness.model;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Data;

import javax.persistence.*;
import java.io.Serializable;
import java.util.UUID;

@Entity
@Table(name = "data_tracking")
@Data
public class DataTracking implements Serializable {
    @Id
    private UUID dataTrackingId;

    @Column(name = "tracking_at")
    private Long trackingAt;

    @Column(name = "is_deleted", columnDefinition = "boolean default false", nullable = false)
    private boolean isDeleted;

    @Column(name = "image_url", nullable = false)
    private String imageUrl;

    @ManyToOne
    @JoinColumn(name = "user_device_id")
    @JsonBackReference
    @JsonIgnore
    private UserDevice userDevice;

    public DataTracking() {
        this.dataTrackingId = UUID.randomUUID();
    }
}
