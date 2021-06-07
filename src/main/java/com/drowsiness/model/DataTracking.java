package com.drowsiness.model;

import com.fasterxml.jackson.annotation.JsonBackReference;
import lombok.Data;
import org.hibernate.annotations.GenericGenerator;
import org.hibernate.annotations.Type;

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

    @ManyToOne
    @JoinColumn(name = "user_device_id")
    @JsonBackReference
    private UserDevice userDevice;

    public DataTracking() {
        this.dataTrackingId = UUID.randomUUID();
    }
}
