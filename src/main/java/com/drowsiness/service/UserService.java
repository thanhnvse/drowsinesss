package com.drowsiness.service;

import com.drowsiness.model.User;

import java.util.List;
import java.util.Optional;

public interface UserService {
    List<User> findAllUser();
    Optional<User> findUserById(String id);
    User findUserByUserId(String id);
    User saveUser(User user);
    void removeUser(User user);
}
