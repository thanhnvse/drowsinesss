package com.drowsiness.dto.role;

import lombok.Data;

import java.io.Serializable;
import java.util.UUID;

@Data
public class RoleResponseDTO implements Serializable {
    private UUID roleId;
    private String roleName;
}
