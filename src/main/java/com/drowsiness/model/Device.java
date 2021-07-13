package com.drowsiness.model;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Data;
import org.hibernate.annotations.GenericGenerator;
import org.hibernate.annotations.Type;

import javax.persistence.*;
import java.io.Serializable;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Entity
@Table(name = "device")
@Data
public class Device implements Serializable {
    @Id
    @Column(name = "device_id")
    private UUID deviceId;

    @Column(name = "device_name",nullable = false, length = 50)
    private String deviceName;


    @Column(name = "created_at", nullable = false, updatable = false)
    private Long createdAt;

    @Column(name = "updated_at")
    private Long updatedAt;

    @ManyToOne
    @JoinColumn(
            name = "firmware_id"
    )
    private Firmware firmware;

    @OneToMany(mappedBy = "device", cascade = CascadeType.ALL)
    @JsonIgnore
    private List<UserDevice> userDevices;

    public Device() {
        this.deviceId = UUID.randomUUID();
    }
}
