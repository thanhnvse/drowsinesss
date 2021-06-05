package com.drowsiness.dto.user;

import lombok.Data;

import java.io.Serializable;

@Data
public class UserResponseDTO implements Serializable {
    private String username;

    private String fullName;

    private String password;

    private String email;

    private Long createdAt;

    private boolean isActive;
}
