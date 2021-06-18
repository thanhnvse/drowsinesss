package com.drowsiness.model;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Data;

import javax.persistence.*;
import java.io.Serializable;
import java.util.List;
import java.util.UUID;

@Entity
@Table(name = "role")
@Data
public class Role implements Serializable {
    @Id
    @Column(name = "role_id")
    private UUID roleId;

    @Column(name = "role_name",nullable = false, length = 50)
    private String roleName;

    @OneToMany(mappedBy = "role", cascade = CascadeType.ALL)
    @JsonIgnore
    @JsonBackReference
    private List<User> users;

    public Role() {
        this.roleId = UUID.randomUUID();
    }
}
