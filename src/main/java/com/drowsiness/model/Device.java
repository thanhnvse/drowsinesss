package com.drowsiness.model;

import lombok.Data;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;
import java.io.Serializable;
import java.util.List;
import java.util.Optional;

@Entity
@Table(name = "device")
@Data
public class Device implements Serializable {
    @Id
    @Column(name = "device_id", columnDefinition = "CHAR(32)")
    @GeneratedValue(generator = "uuid")
    @GenericGenerator(name = "uuid", strategy = "uuid")
    private String deviceId;

    @Column(name = "device_name",nullable = false, length = 50)
    private String deviceName;


    @Column(name = "created_at", nullable = false, updatable = false)
    private Long createdAt;

    @Column(name = "updated_at")
    private Long updatedAt;

    @OneToMany(mappedBy = "device", cascade = CascadeType.ALL)
    private List<UserDevice> userDevices;
}
