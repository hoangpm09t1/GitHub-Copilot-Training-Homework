package com.example.copilot.api.service;

import com.example.copilot.core.dto.UserDTO;
import com.example.copilot.core.entity.User;
import com.example.copilot.core.enums.Role;
import com.example.copilot.service.implementation.UserServiceImpl;
import com.example.copilot.service.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class UserServiceImplTest {

    @Mock
    private UserRepository userRepository;

    @Mock
    private PasswordEncoder passwordEncoder;

    @InjectMocks
    private UserServiceImpl userService;

    private User user;
    private UserDTO userDTO;

    @BeforeEach
    void setUp() {
        user = new User(1L, "John Doe", "john@example.com", "encodedPassword", Role.USER);
        userDTO = new UserDTO(null, "John Doe", "john@example.com", "Password@123", Role.USER);
    }

    @Test
    void addUser_shouldCreateAndReturnUser() {
        when(passwordEncoder.encode(anyString())).thenReturn("encodedPassword");
        when(userRepository.save(any(User.class))).thenReturn(user);

        User result = userService.addUser(userDTO);

        assertThat(result).isNotNull();
        assertThat(result.getEmail()).isEqualTo("john@example.com");
        verify(userRepository).save(any(User.class));
    }

    @Test
    void getUserById_shouldReturnUser_whenExists() {
        when(userRepository.findById(1L)).thenReturn(Optional.of(user));

        User result = userService.getUserById(1L);

        assertThat(result.getId()).isEqualTo(1L);
    }

    @Test
    void getUserById_shouldThrowException_whenNotFound() {
        when(userRepository.findById(anyLong())).thenReturn(Optional.empty());

        assertThatThrownBy(() -> userService.getUserById(99L))
            .isInstanceOf(RuntimeException.class)
            .hasMessageContaining("User not found with id: 99");
    }

    @Test
    void getAllUsers_shouldReturnAllUsers() {
        when(userRepository.findAll()).thenReturn(List.of(user));

        List<User> result = userService.getAllUsers();

        assertThat(result).hasSize(1);
    }

    @Test
    void updateUser_shouldUpdateAndReturnUser() {
        when(userRepository.findById(1L)).thenReturn(Optional.of(user));
        when(passwordEncoder.encode(anyString())).thenReturn("encodedPassword");
        when(userRepository.save(any(User.class))).thenReturn(user);

        User result = userService.updateUser(1L, userDTO);

        assertThat(result).isNotNull();
        verify(userRepository).save(any(User.class));
    }

    @Test
    void updateUser_shouldThrowException_whenNotFound() {
        when(userRepository.findById(anyLong())).thenReturn(Optional.empty());

        assertThatThrownBy(() -> userService.updateUser(99L, userDTO))
            .isInstanceOf(RuntimeException.class)
            .hasMessageContaining("User not found with id: 99");
    }

    @Test
    void deleteUser_shouldDeleteUser_whenExists() {
        when(userRepository.existsById(1L)).thenReturn(true);

        userService.deleteUser(1L);

        verify(userRepository).deleteById(1L);
    }

    @Test
    void deleteUser_shouldThrowException_whenNotFound() {
        when(userRepository.existsById(anyLong())).thenReturn(false);

        assertThatThrownBy(() -> userService.deleteUser(99L))
            .isInstanceOf(RuntimeException.class)
            .hasMessageContaining("User not found with id: 99");
    }

    @Test
    void findByEmail_shouldReturnUser_whenExists() {
        when(userRepository.findByEmail("john@example.com")).thenReturn(Optional.of(user));

        User result = userService.findByEmail("john@example.com");

        assertThat(result.getEmail()).isEqualTo("john@example.com");
    }

    @Test
    void findByEmail_shouldThrowException_whenNotFound() {
        when(userRepository.findByEmail(anyString())).thenReturn(Optional.empty());

        assertThatThrownBy(() -> userService.findByEmail("notfound@example.com"))
            .isInstanceOf(RuntimeException.class)
            .hasMessageContaining("User not found with email");
    }
}
