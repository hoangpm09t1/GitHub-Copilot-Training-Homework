package com.example.copilot.service.implementation;

import com.example.copilot.core.dto.CreateOrderRequestDTO;
import com.example.copilot.core.dto.OrderDTO;
import com.example.copilot.core.dto.OrderItemDTO;
import com.example.copilot.core.entity.Order;
import com.example.copilot.core.entity.OrderItem;
import com.example.copilot.core.entity.Product;
import com.example.copilot.core.entity.User;
import com.example.copilot.core.enums.OrderStatus;
import com.example.copilot.service.repository.OrderRepository;
import com.example.copilot.service.repository.ProductRepository;
import com.example.copilot.service.repository.UserRepository;
import com.example.copilot.service.service.OrderService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class OrderServiceImpl implements OrderService {

    private final OrderRepository orderRepository;
    private final ProductRepository productRepository;
    private final UserRepository userRepository;

    @Override
    @Transactional
    @CacheEvict(value = "products", allEntries = true)
    public OrderDTO placeOrder(CreateOrderRequestDTO request) {
        log.info("Placing order for userId: {}", request.getUserId());

        // 1. Find user
        User user = userRepository.findById(request.getUserId())
                .orElseThrow(() -> new RuntimeException("User not found with id: " + request.getUserId()));

        // 2. Create Order
        Order order = Order.builder()
                .orderDate(LocalDateTime.now())
                .status(OrderStatus.PENDING)
                .user(user)
                .orderItems(new ArrayList<>())
                .build();

        List<OrderItem> orderItems = new ArrayList<>();

        for (CreateOrderRequestDTO.OrderItemRequest itemRequest : request.getItems()) {
            // Validate product stock
            Product product = productRepository.findById(itemRequest.getProductId())
                    .orElseThrow(() -> new RuntimeException("Product not found with id: " + itemRequest.getProductId()));

            if (product.getStockQuantity() < itemRequest.getQuantity()) {
                throw new com.example.copilot.service.exception.InsufficientStockException(
                        "Insufficient stock for product: " + product.getName() +
                        ". Available: " + product.getStockQuantity() +
                        ", Requested: " + itemRequest.getQuantity());
            }

            // 3. Reduce stockQuantity
            product.setStockQuantity(product.getStockQuantity() - itemRequest.getQuantity());
            // 4. Save updated Product
            productRepository.save(product);

            // 5. Create OrderItem with price snapshot
            OrderItem orderItem = OrderItem.builder()
                    .quantity(itemRequest.getQuantity())
                    .price(product.getPrice())
                    .order(order)
                    .product(product)
                    .build();
            orderItems.add(orderItem);
        }

        // 6. Link OrderItems to Order and save
        order.setOrderItems(orderItems);
        // 7. Save Order (cascades to OrderItems)
        Order savedOrder = orderRepository.save(order);

        log.info("Order placed successfully with id: {}", savedOrder.getId());
        return toDTO(savedOrder);
    }

    @Override
    public OrderDTO getOrderById(Long orderId) {
        Order order = orderRepository.findById(orderId)
                .orElseThrow(() -> new RuntimeException("Order not found with id: " + orderId));
        return toDTO(order);
    }

    @Override
    @Transactional
    @CacheEvict(value = "products", allEntries = true)
    public OrderDTO cancelOrder(Long orderId) {
        log.info("Cancelling order with id: {}", orderId);

        Order order = orderRepository.findById(orderId)
                .orElseThrow(() -> new RuntimeException("Order not found with id: " + orderId));

        if (order.getStatus() == OrderStatus.SHIPPED
                || order.getStatus() == OrderStatus.DELIVERED
                || order.getStatus() == OrderStatus.CANCELLED) {
            throw new IllegalStateException(
                    "Cannot cancel order in status: " + order.getStatus());
        }

        for (OrderItem item : order.getOrderItems()) {
            Product product = item.getProduct();
            product.setStockQuantity(product.getStockQuantity() + item.getQuantity());
            productRepository.save(product);
        }

        order.setStatus(OrderStatus.CANCELLED);
        Order saved = orderRepository.save(order);

        log.info("Order {} cancelled successfully", orderId);
        return toDTO(saved);
    }

    @Override
    public List<OrderDTO> getAllOrders() {
        return orderRepository.findAll().stream()
                .map(this::toDTO)
                .collect(Collectors.toList());
    }

    @Override
    public List<OrderDTO> getOrdersByUserId(Long userId) {
        return orderRepository.findByUserId(userId).stream()
                .map(this::toDTO)
                .collect(Collectors.toList());
    }

    private OrderDTO toDTO(Order order) {
        List<OrderItemDTO> itemDTOs = order.getOrderItems().stream()
                .map(item -> OrderItemDTO.builder()
                        .id(item.getId())
                        .productId(item.getProduct().getId())
                        .productName(item.getProduct().getName())
                        .quantity(item.getQuantity())
                        .price(item.getPrice())
                        .build())
                .collect(Collectors.toList());

        return OrderDTO.builder()
                .id(order.getId())
                .orderDate(order.getOrderDate())
                .status(order.getStatus())
                .userId(order.getUser().getId())
                .orderItems(itemDTOs)
                .build();
    }
}
