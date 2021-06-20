package com.drowsiness.service;


import com.drowsiness.model.Role;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface RoleService {
    List<Role> findAllRole();
    Optional<Role> findRoleById(UUID id);
    Role findRoleByRoleName(String roleName);
    Role findRoleByRoleId(UUID id);
    Role findRoleByUsername(String username);
    Role saveRole(Role role);
    void removeRole(Role role);
}
