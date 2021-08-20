package com.drowsiness.controller;

import com.drowsiness.dto.response.ApiResult;
import com.drowsiness.dto.response.SearchListResult;
import com.drowsiness.dto.response.SearchResult;
import com.drowsiness.dto.user.UserCreateDTO;
import com.drowsiness.dto.user.UserResponseDTO;
import com.drowsiness.dto.user.UserUpdateDTO;
import com.drowsiness.exception.ResourceNotFoundException;
import com.drowsiness.model.User;
import com.drowsiness.service.RoleService;
import com.drowsiness.service.UserService;
import com.drowsiness.utils.StaticFuntion;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

//import org.springframework.security.crypto.password.PasswordEncoder;
//import org.springframework.security.crypto.password.PasswordEncoder;

@CrossOrigin(origins = "*", maxAge = 3600)
@RestController
@RequestMapping("/api/v1")
public class UserController {
    private UserService userService;
    private RoleService roleService;
    private ModelMapper modelMapper;
    private PasswordEncoder passwordEncoder;

    @Autowired
    public void setUserService(UserService userService) {
        this.userService = userService;
    }

    @Autowired
    public void setRoleService(RoleService roleService) {
        this.roleService = roleService;
    }

    @Autowired
    public void setModelMapper(ModelMapper modelMapper) {
        this.modelMapper = modelMapper;
    }

    @Autowired
    public void setPasswordEncoder(PasswordEncoder passwordEncoder) {
        this.passwordEncoder = passwordEncoder;
    }

    @GetMapping("/users")
    public ResponseEntity<?> getAllUsers() {
        List<User> userList = userService.findAllUser();
        SearchListResult<?> result = new SearchListResult<>(userList);
        return ResponseEntity.status(HttpStatus.OK).body(result);
    }

    @GetMapping("/users/user")
    public ResponseEntity<?> getAllUsersWithoutAdminRole() {
        List<User> userList = userService.findAllUserByAdminRole();
        SearchListResult<?> result = new SearchListResult<>(userList);
        return ResponseEntity.status(HttpStatus.OK).body(result);
    }

    @GetMapping("/users/{userId}")
    public ResponseEntity<?> getUserById(@PathVariable UUID userId) {
        Optional<User> searchUser = userService.findUserById(userId);
        SearchResult<?> result = !searchUser.equals(Optional.empty())
        ? new SearchResult<>(searchUser.get()): new SearchResult<>();

        return ResponseEntity.status(HttpStatus.OK).body(result);
    }

    @PostMapping("/users")
    public ResponseEntity<?> createUser(@RequestBody UserCreateDTO userDTO) {
        User reqUser = modelMapper.map(userDTO, User.class);
        reqUser.setPassword(passwordEncoder.encode(userDTO.getPassword()));
//        reqUser.setPassword(userDTO.getPassword());
        reqUser.setActive(true);
        reqUser.setCreatedAt(StaticFuntion.getDate());
        reqUser.setPhoneNumber(userDTO.getPhoneNumber());
        reqUser.setRole(roleService.findRoleByRoleName("ROLE_USER"));
        User createdUser = userService.saveUser(reqUser);

        UserResponseDTO userResponseDTO = modelMapper.map(createdUser, UserResponseDTO.class);
        ApiResult<?> apiResult = new ApiResult<>(userResponseDTO,"Your account has been created successfully");
        return ResponseEntity.status(HttpStatus.CREATED).body(apiResult);
    }

    @PutMapping("/users/{userId}")
    public ResponseEntity<?> updateUser(@PathVariable UUID userId, @RequestBody UserUpdateDTO userRequest) {
        return userService.findUserById(userId).map(user -> {
            user.setFullName(userRequest.getFullName());
            if(userRequest.getPassword()!=null){
                user.setPassword(passwordEncoder.encode(userRequest.getPassword()));
            }
//            user.setPassword(userRequest.getPassword());
            user.setEmail(userRequest.getEmail());
            user.setActive(userRequest.isActive());
            user.setAvatar(userRequest.getAvatar());
            user.setUpdatedAt(StaticFuntion.getDate());
            userService.saveUser(user);
            return ResponseEntity.status(HttpStatus.OK).body(new ApiResult<>(userRequest,"Your account has been updated successfully"));
        }).orElseThrow(() -> new ResourceNotFoundException("User not found with id " + userId));
    }

    @PutMapping("/user/{userId}/ban")
    public ResponseEntity<?> banUser(@PathVariable UUID userId, @RequestParam("active") boolean active) {
        return userService.findUserById(userId).map(user -> {
            user.setActive(active);
            userService.saveUser(user);
            return ResponseEntity.status(HttpStatus.OK).body(new ApiResult<>(user,"Your account has been updated successfully"));
        }).orElseThrow(() -> new ResourceNotFoundException("User not found with id " + userId));
    }

    @DeleteMapping("/users/{userId}")
    public ResponseEntity<?> removeUser(@PathVariable UUID userId) {
        return userService.findUserById(userId).map(user -> {
            userService.removeUser(user);
            return ResponseEntity.ok().build();
        }).orElseThrow(() -> new ResourceNotFoundException("User not found with id " + userId));
    }

    
}
