package com.drowsiness.dto.user;

import lombok.Data;

@Data
public class UserLoginByGGDTO {
    private String fullName;
    private String email;
    private String avatar;
    private String uuid;
    private String phoneNumber;
}
