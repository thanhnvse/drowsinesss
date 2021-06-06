package com.drowsiness.service;


import com.drowsiness.model.Role;

import java.util.List;
import java.util.Optional;

public interface RoleService {
    List<Role> findAllRole();
    Optional<Role> findRoleById(String id);
    Role findRoleByRoleName(String roleName);
    Role findRoleByRoleId(String id);
    Role saveRole(Role role);
    void removeRole(Role role);
}
