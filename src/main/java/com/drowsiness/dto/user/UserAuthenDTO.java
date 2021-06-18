package com.drowsiness.dto.user;

import com.drowsiness.dto.role.RoleResponseDTO;
import lombok.Data;

import java.util.UUID;

@Data
public class UserAuthenDTO {
    private UUID userId;

    private String username;

    private String fullName;

    private String password;

    private String email;

    private String phoneNumber;

    private boolean isActive;

    private RoleResponseDTO roleResponseDTO;

    public UserAuthenDTO() {
    }

    public UserAuthenDTO(String username, String fullName, String password, String email, String phoneNumber) {
        this.username = username;
        this.fullName = fullName;
        this.password = password;
        this.email = email;
        this.phoneNumber = phoneNumber;
    }
}
