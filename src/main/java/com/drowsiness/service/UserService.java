package com.drowsiness.service;

import com.drowsiness.model.User;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface UserService {
    List<User> findAllUser();
    Optional<User> findUserById(UUID id);
    User findUserByUserId(UUID id);
    User saveUser(User user);
    void removeUser(User user);
    User login(String username, String password);
    User findUserByUsername(String username);
    User findUserByEmail(String email);
    List<User> findAllUserByAdminRole();
}
