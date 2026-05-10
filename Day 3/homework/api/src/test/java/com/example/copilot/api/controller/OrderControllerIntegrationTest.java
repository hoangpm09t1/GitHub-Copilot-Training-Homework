package com.example.copilot.api.controller;

import com.example.copilot.core.dto.CreateOrderRequestDTO;
import com.example.copilot.core.dto.OrderDTO;
import com.example.copilot.core.enums.OrderStatus;
import com.example.copilot.service.exception.InsufficientStockException;
import com.example.copilot.service.service.OrderService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.security.test.web.servlet.setup.SecurityMockMvcConfigurers;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import java.time.LocalDateTime;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
class OrderControllerIntegrationTest {

    @Autowired private WebApplicationContext webApplicationContext;
    private final ObjectMapper objectMapper = new ObjectMapper();
    @MockitoBean private OrderService orderService;

    private MockMvc mockMvc;

    @BeforeEach
    void setUp() {
        mockMvc = MockMvcBuilders.webAppContextSetup(webApplicationContext)
                .apply(SecurityMockMvcConfigurers.springSecurity())
                .build();
    }

    @Test
    @WithMockUser
    void placeOrder_shouldReturn201_whenSuccess() throws Exception {
        OrderDTO orderDTO = OrderDTO.builder()
                .id(1L)
                .orderDate(LocalDateTime.now())
                .status(OrderStatus.PENDING)
                .userId(1L)
                .orderItems(List.of())
                .build();

        when(orderService.placeOrder(any())).thenReturn(orderDTO);

        CreateOrderRequestDTO request = new CreateOrderRequestDTO();
        request.setUserId(1L);
        request.setItems(List.of(new CreateOrderRequestDTO.OrderItemRequest(1L, 2)));

        mockMvc.perform(post("/api/orders")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.status").value("PENDING"))
                .andExpect(jsonPath("$.userId").value(1));
    }

    @Test
    @WithMockUser
    void placeOrder_shouldReturn409_whenInsufficientStock() throws Exception {
        when(orderService.placeOrder(any()))
                .thenThrow(new InsufficientStockException("Insufficient stock for product: iPhone 15"));

        CreateOrderRequestDTO request = new CreateOrderRequestDTO();
        request.setUserId(1L);
        request.setItems(List.of(new CreateOrderRequestDTO.OrderItemRequest(1L, 99999)));

        mockMvc.perform(post("/api/orders")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isConflict());
    }

    @Test
    void placeOrder_shouldReturn401_whenNoAuth() throws Exception {
        CreateOrderRequestDTO request = new CreateOrderRequestDTO();
        request.setUserId(1L);
        request.setItems(List.of(new CreateOrderRequestDTO.OrderItemRequest(1L, 1)));

        mockMvc.perform(post("/api/orders")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isUnauthorized());
    }

    @Test
    @WithMockUser
    void getAllOrders_shouldReturn200() throws Exception {
        when(orderService.getAllOrders()).thenReturn(List.of());

        mockMvc.perform(get("/api/orders"))
                .andExpect(status().isOk());
    }

    @Test
    @WithMockUser
    void getOrdersByUser_shouldReturn200() throws Exception {
        when(orderService.getOrdersByUserId(1L)).thenReturn(List.of());

        mockMvc.perform(get("/api/users/1/orders"))
                .andExpect(status().isOk());
    }
}
