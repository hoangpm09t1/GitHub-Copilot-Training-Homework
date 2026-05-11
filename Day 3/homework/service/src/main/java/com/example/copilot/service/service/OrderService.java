package com.example.copilot.service.service;

import com.example.copilot.core.dto.CreateOrderRequestDTO;
import com.example.copilot.core.dto.OrderDTO;

import java.util.List;

public interface OrderService {

    OrderDTO placeOrder(CreateOrderRequestDTO request);

    OrderDTO getOrderById(Long orderId);

    List<OrderDTO> getAllOrders();

    List<OrderDTO> getOrdersByUserId(Long userId);

    OrderDTO cancelOrder(Long orderId);
}
