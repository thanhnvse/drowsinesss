package com.drowsiness.service.impl;

import com.drowsiness.model.User;
import com.drowsiness.repository.UserRepository;
import com.drowsiness.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
public class UserServiceImpl implements UserService {
    private UserRepository userRepository;

    @Autowired
    public void setUserRepository(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public List<User> findAllUser() {
        return userRepository.findAll();
    }

    @Override
    public Optional<User> findUserById(UUID id) {
        return userRepository.findById(id);
    }

    @Override
    public User findUserByUserId(UUID id) {
        return userRepository.findById(id).get();
    }

    @Override
    public User saveUser(User user) {
        return userRepository.save(user);
    }

    @Override
    public void removeUser(User user) {
        userRepository.delete(user);
    }
}
