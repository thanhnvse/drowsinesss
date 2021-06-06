package com.drowsiness.controller;

import com.drowsiness.dto.response.ApiResult;
import com.drowsiness.dto.response.SearchListResult;
import com.drowsiness.dto.response.SearchResult;
import com.drowsiness.dto.role.RoleDTO;
import com.drowsiness.dto.user.UserCreateDTO;
import com.drowsiness.dto.user.UserResponseDTO;
import com.drowsiness.dto.user.UserUpdateDTO;
import com.drowsiness.exception.ResourceNotFoundException;
import com.drowsiness.model.Role;
import com.drowsiness.model.User;
import com.drowsiness.service.RoleService;
import com.drowsiness.utils.StaticFuntion;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/v1")
public class RoleController {
    private RoleService roleService;
    private ModelMapper modelMapper;

    @Autowired
    public void setRoleService(RoleService roleService) {
        this.roleService = roleService;
    }

    @Autowired
    public void setModelMapper(ModelMapper modelMapper) {
        this.modelMapper = modelMapper;
    }

    @GetMapping("/roles")
    public ResponseEntity<?> getAllRoles() {
        List<Role> roleList = roleService.findAllRole();
        SearchListResult<?> result = new SearchListResult<>(roleList);
        return ResponseEntity.status(HttpStatus.OK).body(result);
    }

    @GetMapping("/roles/{roleId}")
    public ResponseEntity<?> getRoleById(@PathVariable String roleId) {
        Optional<Role> searchRole = roleService.findRoleById(roleId);
        SearchResult<?> result = !searchRole.equals(Optional.empty())
                ? new SearchResult<>(searchRole.get()): new SearchResult<>();

        return ResponseEntity.status(HttpStatus.OK).body(result);
    }

    @PostMapping("/roles")
    public ResponseEntity<?> createRole(@RequestBody RoleDTO roleDTO) {
        Role reqRole = modelMapper.map(roleDTO, Role.class);

        Role createdRole = roleService.saveRole(reqRole);

        RoleDTO roleResponseDTO = modelMapper.map(reqRole, RoleDTO.class);
        ApiResult<?> apiResult = new ApiResult<>(roleResponseDTO,"Your role has been created successfully");
        return ResponseEntity.status(HttpStatus.CREATED).body(apiResult);
    }

    @PutMapping("/roles/{roleId}")
    public ResponseEntity<?> updateRole(@PathVariable String roleId, @RequestBody RoleDTO roleRequest) {
        return roleService.findRoleById(roleId).map(role -> {
            role.setRoleName(roleRequest.getRoleName());
            roleService.saveRole(role);
            return ResponseEntity.status(HttpStatus.OK).body(new ApiResult<>(roleRequest,"Your role has been updated successfully"));
        }).orElseThrow(() -> new ResourceNotFoundException("Role not found with id " + roleId));
    }

    @DeleteMapping("/roles/{roleId}")
    public ResponseEntity<?> removeRole(@PathVariable String roleId) {
        return roleService.findRoleById(roleId).map(role -> {
            roleService.removeRole(role);
            return ResponseEntity.ok().build();
        }).orElseThrow(() -> new ResourceNotFoundException("Role not found with id " + roleId));
    }
}
