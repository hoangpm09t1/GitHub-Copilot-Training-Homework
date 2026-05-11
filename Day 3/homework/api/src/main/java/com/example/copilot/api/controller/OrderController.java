package com.example.copilot.api.controller;

import com.example.copilot.core.dto.CreateOrderRequestDTO;
import com.example.copilot.core.dto.OrderDTO;
import com.example.copilot.service.service.OrderService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
public class OrderController {

    private final OrderService orderService;

    @PostMapping("/api/orders")
    @Operation(summary = "Place a new order", security = @SecurityRequirement(name = "bearerAuth"))
    public ResponseEntity<OrderDTO> placeOrder(@Valid @RequestBody CreateOrderRequestDTO request) {
        return ResponseEntity.status(HttpStatus.CREATED).body(orderService.placeOrder(request));
    }

    @GetMapping("/api/orders")
    @Operation(summary = "Get all orders", security = @SecurityRequirement(name = "bearerAuth"))
    public ResponseEntity<List<OrderDTO>> getAllOrders() {
        return ResponseEntity.ok(orderService.getAllOrders());
    }

    @GetMapping("/api/orders/{orderId}")
    @Operation(summary = "Get order by ID", security = @SecurityRequirement(name = "bearerAuth"))
    public ResponseEntity<OrderDTO> getOrderById(@PathVariable Long orderId) {
        return ResponseEntity.ok(orderService.getOrderById(orderId));
    }

    @GetMapping("/api/users/{userId}/orders")
    @Operation(summary = "Get orders by user", security = @SecurityRequirement(name = "bearerAuth"))
    public ResponseEntity<List<OrderDTO>> getOrdersByUser(@PathVariable Long userId) {
        return ResponseEntity.ok(orderService.getOrdersByUserId(userId));
    }

    @PatchMapping("/api/orders/{orderId}/cancel")
    @Operation(summary = "Cancel an order", security = @SecurityRequirement(name = "bearerAuth"))
    public ResponseEntity<OrderDTO> cancelOrder(@PathVariable Long orderId) {
        return ResponseEntity.ok(orderService.cancelOrder(orderId));
    }
}
