package com.drowsiness.service.impl;

import com.drowsiness.model.Role;
import com.drowsiness.repository.RoleRepository;
import com.drowsiness.service.RoleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

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
    public Optional<Role> findRoleById(String id) {
        return roleRepository.findById(id);
    }

    @Override
    public Role findRoleByRoleId(String id) {
        return roleRepository.findById(id).get();
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
