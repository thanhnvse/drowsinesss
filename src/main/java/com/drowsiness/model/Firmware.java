package com.drowsiness.model;


import com.fasterxml.jackson.annotation.JsonBackReference;
import lombok.Data;

import javax.persistence.*;
import java.io.Serializable;
import java.util.List;
import java.util.UUID;

@Entity
@Table(name = "firmware")
@Data
public class Firmware implements Serializable {
    @Id
    @Column(
            name = "firmware_id"
    )

    private UUID firmwareId = UUID.randomUUID();
    @Column(
            name = "firmware_name",
            nullable = false,
            length = 50
    )

    private String firmwareName;
    @Column(
            name = "description",
            nullable = false,
            length = 256
    )

    private String description;
    @GeneratedValue(
            strategy = GenerationType.IDENTITY
    )
    @Column(
            name = "version",
            nullable = false,
            updatable = false
    )

    private int version;
    @Column(
            name = "model_url",
            nullable = false
    )

    private String modelUrl;
    @Column(
            name = "time_detection",
            nullable = false
    )

    private float timeDetection;
    @Column(
            name = "created_at",
            nullable = false,
            updatable = false
    )

    private Long createdAt;
    @Column(
            name = "updated_at"
    )

    private Long updatedAt;
    @OneToMany(
            mappedBy = "firmware",
            cascade = {CascadeType.ALL}
    )

    @JsonBackReference
    private List<Device> devices;
}
