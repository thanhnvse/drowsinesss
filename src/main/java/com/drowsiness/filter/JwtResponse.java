package com.drowsiness.filter;

import lombok.Data;

import java.util.UUID;

@Data
public class JwtResponse {
    private UUID userId;

    private String username;

    private String fullName;

    private String password;

    private String phoneNumber;

    private String email;

    private String avatar;

    private boolean isActive;

    private Long createdAt;

    private Long updatedAt;

    private String token;
    private String type = "Bearer";

    public JwtResponse(String accessToken) {
        this.token = accessToken;
    }
    public JwtResponse(UUID userId, String username, String fullName, String password, String phoneNumber, String email, String avatar, boolean isActive, Long createdAt, Long updatedAt, String token) {
        this.userId = userId;
        this.username = username;
        this.fullName = fullName;
        this.password = password;
        this.phoneNumber = phoneNumber;
        this.email = email;
        this.avatar = avatar;
        this.isActive = isActive;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
        this.token = token;
    }
}
