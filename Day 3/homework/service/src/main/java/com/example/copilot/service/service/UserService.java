package com.example.copilot.service.service;

import com.example.copilot.core.dto.UserDTO;
import com.example.copilot.core.entity.User;

import java.util.List;

public interface UserService {
    User addUser(UserDTO userDTO);
    User getUserById(Long id);
    List<User> getAllUsers();
    User updateUser(Long id, UserDTO userDTO);
    void deleteUser(Long id);
    User findByEmail(String email);
}
