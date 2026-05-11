package com.example.copilot.api.repository;

import com.example.copilot.core.entity.Order;
import com.example.copilot.core.entity.User;
import com.example.copilot.core.enums.OrderStatus;
import com.example.copilot.core.enums.Role;
import com.example.copilot.service.repository.OrderRepository;
import com.example.copilot.service.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
class OrderRepositoryTest {

    @Autowired private OrderRepository orderRepository;
    @Autowired private UserRepository userRepository;

    private User user;

    @BeforeEach
    void setUp() {
        orderRepository.deleteAll();
        userRepository.deleteAll();

        user = new User(null, "John", "john@example.com", "encoded", Role.USER);
        user = userRepository.save(user);

        Order pending = Order.builder()
                .orderDate(LocalDateTime.now().minusDays(2))
                .status(OrderStatus.PENDING)
                .user(user)
                .orderItems(new ArrayList<>())
                .build();
        orderRepository.save(pending);

        Order shipped = Order.builder()
                .orderDate(LocalDateTime.now().minusDays(1))
                .status(OrderStatus.SHIPPED)
                .user(user)
                .orderItems(new ArrayList<>())
                .build();
        orderRepository.save(shipped);
    }

    @Test
    void findByUserId_shouldReturnOrdersForUser() {
        List<Order> orders = orderRepository.findByUserId(user.getId());

        assertThat(orders).hasSize(2);
        orders.forEach(o -> assertThat(o.getUser().getId()).isEqualTo(user.getId()));
    }

    @Test
    void findByStatusAndOrderDateBefore_shouldReturnMatchingOrders() {
        List<Order> result = orderRepository.findByStatusAndOrderDateBefore(
                OrderStatus.PENDING, LocalDateTime.now());

        assertThat(result).hasSize(1);
        assertThat(result.get(0).getStatus()).isEqualTo(OrderStatus.PENDING);
    }

    @Test
    void findByUserId_shouldReturnEmpty_whenUserHasNoOrders() {
        User other = userRepository.save(
                new User(null, "Jane", "jane@example.com", "encoded", Role.USER));

        List<Order> orders = orderRepository.findByUserId(other.getId());

        assertThat(orders).isEmpty();
    }
}