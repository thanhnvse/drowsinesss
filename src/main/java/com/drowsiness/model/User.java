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
@Table(name = "account_user")
@Data
public class User implements Serializable {
    @Id
    @Column(name = "user_id")
    private UUID userId;

    @Column(name = "user_name",nullable = false, length = 50)
    private String username;

    @Column(name = "full_name",nullable = false, length = 50)
    private String fullName;

    @Column(nullable = false, length = 50)
    private String password;

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
    private Role role;

    @OneToMany(mappedBy = "accountUser", cascade = CascadeType.ALL)
    private List<UserDevice> userDevices;

    public User() {
        this.userId = UUID.randomUUID();
    }
}
