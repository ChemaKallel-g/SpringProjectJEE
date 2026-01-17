package com.institut.ProjetSpringAC.service;

import com.institut.ProjetSpringAC.entity.User;

import java.util.List;
import java.util.Optional;

public interface UserService {
    void saveUser(User user);

    List<User> getAllUsers();

    Optional<User> getUserById(Long id);

    void updateUser(User user);

    void deleteUser(Long id);
}
