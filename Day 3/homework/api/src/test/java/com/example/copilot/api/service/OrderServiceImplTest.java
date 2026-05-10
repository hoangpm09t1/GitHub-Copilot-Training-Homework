package com.example.copilot.api.service;

import com.example.copilot.core.dto.CreateOrderRequestDTO;
import com.example.copilot.core.dto.OrderDTO;
import com.example.copilot.core.entity.Order;
import com.example.copilot.core.entity.Product;
import com.example.copilot.core.entity.User;
import com.example.copilot.core.enums.OrderStatus;
import com.example.copilot.core.enums.Role;
import com.example.copilot.service.exception.InsufficientStockException;
import com.example.copilot.service.implementation.OrderServiceImpl;
import com.example.copilot.service.repository.OrderRepository;
import com.example.copilot.service.repository.ProductRepository;
import com.example.copilot.service.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class OrderServiceImplTest {

    @Mock private OrderRepository orderRepository;
    @Mock private ProductRepository productRepository;
    @Mock private UserRepository userRepository;

    @InjectMocks
    private OrderServiceImpl orderService;

    private User user;
    private Product product;

    @BeforeEach
    void setUp() {
        user = new User();
        user.setId(1L);
        user.setName("John");
        user.setEmail("john@example.com");
        user.setPassword("encoded");
        user.setRole(Role.USER);

        product = new Product();
        product.setId(1L);
        product.setName("iPhone 15");
        product.setPrice(BigDecimal.valueOf(999.99));
        product.setStockQuantity(10);
    }

    @Test
    void placeOrder_shouldSucceed_whenStockSufficient() {
        CreateOrderRequestDTO request = new CreateOrderRequestDTO();
        request.setUserId(1L);
        request.setItems(List.of(new CreateOrderRequestDTO.OrderItemRequest(1L, 2)));

        Order savedOrder = Order.builder()
                .id(1L)
                .orderDate(LocalDateTime.now())
                .status(OrderStatus.PENDING)
                .user(user)
                .orderItems(new ArrayList<>())
                .build();

        when(userRepository.findById(1L)).thenReturn(Optional.of(user));
        when(productRepository.findById(1L)).thenReturn(Optional.of(product));
        when(productRepository.save(any(Product.class))).thenReturn(product);
        when(orderRepository.save(any(Order.class))).thenReturn(savedOrder);

        OrderDTO result = orderService.placeOrder(request);

        assertNotNull(result);
        assertEquals(OrderStatus.PENDING, result.getStatus());
        assertEquals(1L, result.getUserId());
        verify(productRepository).save(any(Product.class));
    }

    @Test
    void placeOrder_shouldThrowInsufficientStock_whenStockTooLow() {
        product.setStockQuantity(1);

        CreateOrderRequestDTO request = new CreateOrderRequestDTO();
        request.setUserId(1L);
        request.setItems(List.of(new CreateOrderRequestDTO.OrderItemRequest(1L, 5)));

        when(userRepository.findById(1L)).thenReturn(Optional.of(user));
        when(productRepository.findById(1L)).thenReturn(Optional.of(product));

        assertThrows(InsufficientStockException.class, () -> orderService.placeOrder(request));
        verify(orderRepository, never()).save(any());
    }

    @Test
    void placeOrder_shouldReduceStock() {
        CreateOrderRequestDTO request = new CreateOrderRequestDTO();
        request.setUserId(1L);
        request.setItems(List.of(new CreateOrderRequestDTO.OrderItemRequest(1L, 3)));

        Order savedOrder = Order.builder()
                .id(1L).orderDate(LocalDateTime.now())
                .status(OrderStatus.PENDING).user(user).orderItems(new ArrayList<>()).build();

        when(userRepository.findById(1L)).thenReturn(Optional.of(user));
        when(productRepository.findById(1L)).thenReturn(Optional.of(product));
        when(productRepository.save(any(Product.class))).thenAnswer(inv -> {
            Product p = inv.getArgument(0);
            assertEquals(7, p.getStockQuantity());
            return p;
        });
        when(orderRepository.save(any(Order.class))).thenReturn(savedOrder);

        orderService.placeOrder(request);

        verify(productRepository).save(any(Product.class));
    }

    @Test
    void getAllOrders_shouldReturnList() {
        Order order = Order.builder()
                .id(1L).orderDate(LocalDateTime.now())
                .status(OrderStatus.PENDING).user(user).orderItems(new ArrayList<>()).build();

        when(orderRepository.findAll()).thenReturn(List.of(order));

        List<OrderDTO> result = orderService.getAllOrders();

        assertEquals(1, result.size());
    }

    @Test
    void getOrdersByUserId_shouldReturnUserOrders() {
        Order order = Order.builder()
                .id(1L).orderDate(LocalDateTime.now())
                .status(OrderStatus.CONFIRMED).user(user).orderItems(new ArrayList<>()).build();

        when(orderRepository.findByUserId(1L)).thenReturn(List.of(order));

        List<OrderDTO> result = orderService.getOrdersByUserId(1L);

        assertEquals(1, result.size());
        assertEquals(1L, result.get(0).getUserId());
    }

    @Test
    void placeOrder_shouldThrowException_whenUserNotFound() {
        when(userRepository.findById(99L)).thenReturn(Optional.empty());

        CreateOrderRequestDTO request = new CreateOrderRequestDTO();
        request.setUserId(99L);
        request.setItems(List.of(new CreateOrderRequestDTO.OrderItemRequest(1L, 1)));

        assertThrows(RuntimeException.class, () -> orderService.placeOrder(request));
    }
}
