package com.drowsiness.model;

import com.fasterxml.jackson.annotation.JsonBackReference;
import lombok.Data;
import org.hibernate.annotations.GenericGenerator;
import org.hibernate.annotations.Type;

import javax.persistence.*;
import java.io.Serializable;
import java.util.List;
import java.util.UUID;

@Entity
@Table(name = "user_device")
@Data
public class UserDevice implements Serializable {
    @Id
    @Column(name = "user_device_id")
    private UUID userDeviceId;

    @ManyToOne
    @JoinColumn(name = "user_id")
    @JsonBackReference
    private User accountUser;

    @ManyToOne
    @JoinColumn(name = "device_id")
    @JsonBackReference
    private Device device;

    @Column(name = "is_connected", columnDefinition = "boolean default false", nullable = false)
    private boolean isConnected;

    @Column(name = "connected_at")
    private Long connectedAt;

    @Column(name = "updated_at")
    private Long updatedAt;

    @OneToMany(mappedBy = "userDevice", cascade = CascadeType.ALL)
    private List<DataTracking> dataTrackings;

    public UserDevice() {
        this.userDeviceId = UUID.randomUUID();
    }
}