package com.drowsiness.dto.role;

import lombok.Data;

import java.io.Serializable;

@Data
public class RoleResponseDTO implements Serializable {
    private String roleId;
    private String roleName;
}
