package com.drowsiness.model;

import com.fasterxml.jackson.annotation.JsonBackReference;
import lombok.Data;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;

@Entity
@Table(name = "user_device")
@Data
public class UserDevice {
    @Id
    @Column(name = "user_device_id", columnDefinition = "CHAR(32)")
    @GeneratedValue(generator = "uuid")
    @GenericGenerator(name = "uuid", strategy = "uuid")
    private String userDeviceId;

    @ManyToOne
    @JoinColumn(name = "user_id")
    @JsonBackReference
    private User user;

    @ManyToOne
    @JoinColumn(name = "device_id")
    @JsonBackReference
    private Device device;

    @Column(name = "is_connected", columnDefinition = "tinyint(1) default 0", nullable = false)
    private boolean isConnected;

    @Column(name = "connected_at")
    private Long connectedAt;
}