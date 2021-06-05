package com.drowsiness.controller;

import com.drowsiness.dto.user.UserCreateDTO;
import com.drowsiness.dto.user.UserResponseDTO;
import com.drowsiness.dto.response.ApiResult;
import com.drowsiness.dto.response.SearchListResult;
import com.drowsiness.dto.response.SearchResult;
import com.drowsiness.dto.user.UserUpdateDTO;
import com.drowsiness.exception.ResourceNotFoundException;
import com.drowsiness.model.User;
import com.drowsiness.service.UserService;
import com.drowsiness.utils.StaticFuntion;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
//import org.springframework.security.crypto.password.PasswordEncoder;
//import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/v1")
public class UserController {
    private UserService userService;
    private ModelMapper modelMapper;
//    private PasswordEncoder passwordEncoder;

    @Autowired
    public void setUserService(UserService userService) {
        this.userService = userService;
    }

    @Autowired
    public void setModelMapper(ModelMapper modelMapper) {
        this.modelMapper = modelMapper;
    }

//    @Autowired
//    public void setPasswordEncoder(PasswordEncoder passwordEncoder) {
//        this.passwordEncoder = passwordEncoder;
//    }

    @GetMapping("/users")
    public ResponseEntity<?> getAllUsers() {
        List<User> userList = userService.findAllUser();
        SearchListResult<?> result = new SearchListResult<>(userList);
        return ResponseEntity.status(HttpStatus.OK).body(result);
    }

    @GetMapping("/users/{userId}")
    public ResponseEntity<?> getUserById(@PathVariable String userId) {
        Optional<User> searchUser = userService.findUserById(userId);
        SearchResult<?> result = !searchUser.equals(Optional.empty())
        ? new SearchResult<>(searchUser.get()): new SearchResult<>();

        return ResponseEntity.status(HttpStatus.OK).body(result);
    }

    @PostMapping("/users")
    public ResponseEntity<?> createRenter(@RequestBody UserCreateDTO userDTO) {
        User reqUser = modelMapper.map(userDTO, User.class);
//        reqUser.setPassword(passwordEncoder.encode(userDTO.getPassword()));
        reqUser.setCreatedAt(StaticFuntion.getDate());
        User createdUser = userService.saveUser(reqUser);

        UserResponseDTO userResponseDTO = modelMapper.map(createdUser, UserResponseDTO.class);
        ApiResult<?> apiResult = new ApiResult<>(userResponseDTO,"Your account has been created successfully");
        return ResponseEntity.status(HttpStatus.CREATED).body(apiResult);
    }

    @PutMapping("/users/{userId}")
    public ResponseEntity<?> updateUser(@PathVariable String userId, @RequestBody UserUpdateDTO userRequest) {
        return userService.findUserById(userId).map(user -> {
            user.setUsername(userRequest.getUsername());
            user.setFullName(userRequest.getFullName());
            user.setPassword(userRequest.getPassword());
            user.setEmail(userRequest.getEmail());
            user.setActive(userRequest.isActive());
            user.setAvatar(userRequest.getAvatar());
            user.setUpdatedAt(StaticFuntion.getDate());
            userService.saveUser(user);
            return ResponseEntity.status(HttpStatus.OK).body(new ApiResult<>(userRequest,"Your account has been updated successfully"));
        }).orElseThrow(() -> new ResourceNotFoundException("User not found with id " + userId));
    }

    @DeleteMapping("/users/{userId}")
    public ResponseEntity<?> removeUser(@PathVariable String userId) {
        return userService.findUserById(userId).map(user -> {
            userService.removeUser(user);
            return ResponseEntity.ok().build();
        }).orElseThrow(() -> new ResourceNotFoundException("User not found with id " + userId));
    }
    
}
