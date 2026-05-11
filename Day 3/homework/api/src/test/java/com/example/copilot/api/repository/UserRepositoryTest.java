package com.example.copilot.api.repository;

import com.example.copilot.core.entity.User;
import com.example.copilot.core.enums.Role;
import com.example.copilot.service.repository.OrderRepository;
import com.example.copilot.service.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
class UserRepositoryTest {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private OrderRepository orderRepository;

    @BeforeEach
    void setUp() {
        orderRepository.deleteAll();
        userRepository.deleteAll();
    }

    @Test
    void findByEmail_shouldReturnUser_whenEmailExists() {
        User user = new User(null, "Alice", "alice@example.com", "encoded", Role.USER);
        userRepository.save(user);

        Optional<User> result = userRepository.findByEmail("alice@example.com");

        assertThat(result).isPresent();
        assertThat(result.get().getName()).isEqualTo("Alice");
    }

    @Test
    void findByEmail_shouldReturnEmpty_whenEmailNotFound() {
        Optional<User> result = userRepository.findByEmail("notfound@example.com");

        assertThat(result).isEmpty();
    }

    @Test
    void save_shouldPersistUser() {
        User user = new User(null, "Bob", "bob@example.com", "encoded", Role.ADMIN);

        User saved = userRepository.save(user);

        assertThat(saved.getId()).isNotNull();
        assertThat(userRepository.findById(saved.getId())).isPresent();
    }
}
