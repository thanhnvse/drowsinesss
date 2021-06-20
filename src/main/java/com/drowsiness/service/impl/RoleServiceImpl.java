package com.drowsiness.service.impl;

import com.drowsiness.model.Role;
import com.drowsiness.repository.RoleRepository;
import com.drowsiness.service.RoleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
public class RoleServiceImpl implements RoleService {
    private RoleRepository roleRepository;

    @Autowired
    public void setRoleRepository(RoleRepository roleRepository) {
        this.roleRepository = roleRepository;
    }

    @Override
    public List<Role> findAllRole() {
        return roleRepository.findAll();
    }

    @Override
    public Optional<Role> findRoleById(UUID id) {
        return roleRepository.findById(id);
    }

    @Override
    public Role findRoleByRoleName(String roleName) {
        return roleRepository.findRoleByRoleName(roleName);
    }

    @Override
    public Role findRoleByRoleId(UUID id) {
        return roleRepository.findById(id).get();
    }

    @Override
    public Role findRoleByUsername(String username) {
        return roleRepository.findRoleByUserName(username);
    }

    @Override
    public Role saveRole(Role role) {
        return roleRepository.save(role);
    }

    @Override
    public void removeRole(Role role) {
        roleRepository.delete(role);
    }
}
