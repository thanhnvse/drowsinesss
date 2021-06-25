package com.drowsiness.dto.user;

import lombok.Data;

@Data
public class UserUpdateDTO {
    private String fullName;

    private String password;

    private String email;

    private String avatar;

    private boolean isActive;
}
