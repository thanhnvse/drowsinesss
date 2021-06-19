package com.drowsiness.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Data;

import javax.persistence.*;
import java.io.Serializable;
import java.util.List;
import java.util.UUID;

@Entity
@Table(name = "account_user")
@Data
public class User implements Serializable {
    @Id
    @Column(name = "user_id", unique = true)
    private UUID userId;

    @Column(name = "user_name",nullable = false, length = 50)
    private String username;

    @Column(name = "full_name",nullable = false, length = 50)
    private String fullName;

    @Column(nullable = false, length = 250)
    private String password;

    @Column(name = "phone_number", nullable = false, length = 10)
    private String phoneNumber;

    @Column(nullable = false)
    private String email;

    private String avatar;

    //for MySQL
    @Column(name = "is_active", columnDefinition = "boolean default true", nullable = false)
    private boolean isActive;

    @Column(name = "created_at", nullable = false, updatable = false)
    private Long createdAt;

    @Column(name = "updated_at")
    private Long updatedAt;

    @ManyToOne
    @JoinColumn(name = "role_id")
    @JsonIgnore
    private Role role;

    @OneToMany(mappedBy = "accountUser", cascade = CascadeType.ALL)
    @JsonIgnore
    private List<UserDevice> userDevices;

    public User() {
        this.userId = UUID.randomUUID();
    }

    public User( String fullName, String username, String email, String password, Role role, String phoneNumber) {
        this.username = username;
        this.fullName = fullName;
        this.password = password;
        this.email = email;
        this.role = role;
        this.phoneNumber = phoneNumber;
    }
}
