package com.drowsiness.model;

import lombok.Data;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;
import java.io.Serializable;

@Entity
@Table(name = "user")
@Data
public class User implements Serializable {
    @Id
    @Column(name = "user_id", columnDefinition = "CHAR(32)")
    @GeneratedValue(generator = "uuid")
    @GenericGenerator(name = "uuid", strategy = "uuid")
    private String userId;

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
    @Column(name = "is_active", columnDefinition = "tinyint(1) default 1", nullable = false)
    private boolean isActive;

    @Column(name = "created_at", nullable = false, updatable = false)
    private Long createdAt;

    @Column(name = "updated_at")
    private Long updatedAt;
}
