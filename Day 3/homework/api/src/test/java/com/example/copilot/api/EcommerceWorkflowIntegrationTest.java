package com.example.copilot.api;

import com.example.copilot.core.dto.CategoryDTO;
import com.example.copilot.core.dto.CreateOrderRequestDTO;
import com.example.copilot.core.dto.OrderDTO;
import com.example.copilot.core.dto.ProductDTO;
import com.example.copilot.core.dto.UserDTO;
import com.example.copilot.core.entity.User;
import com.example.copilot.core.enums.OrderStatus;
import com.example.copilot.core.enums.Role;
import com.example.copilot.service.repository.OrderRepository;
import com.example.copilot.service.repository.ProductRepository;
import com.example.copilot.service.repository.UserRepository;
import com.example.copilot.service.service.CategoryService;
import com.example.copilot.service.service.OrderService;
import com.example.copilot.service.service.ProductService;
import com.example.copilot.service.service.UserService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.DirtiesContext;

import java.math.BigDecimal;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

@SpringBootTest
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_CLASS)
class EcommerceWorkflowIntegrationTest {

    @Autowired private UserService userService;
    @Autowired private CategoryService categoryService;
    @Autowired private ProductService productService;
    @Autowired private OrderService orderService;

    @Autowired private UserRepository userRepository;
    @Autowired private ProductRepository productRepository;
    @Autowired private OrderRepository orderRepository;

    @BeforeEach
    void cleanUp() {
        orderRepository.deleteAll();
        productRepository.deleteAll();
        userRepository.deleteAll();
    }

    @Test
    void fullOrderWorkflow_placeAndCancel_shouldRestoreStock() {
        // Arrange: create user
        UserDTO userDTO = new UserDTO(null, "E2E User", "e2e@example.com", "Password@123", Role.USER);
        User user = userService.addUser(userDTO);
        assertThat(user.getId()).isNotNull();

        // Arrange: create category and product (stock = 10)
        CategoryDTO catDTO = new CategoryDTO();
        catDTO.setName("Electronics");
        CategoryDTO savedCat = categoryService.addCategory(catDTO);

        ProductDTO productDTO = ProductDTO.builder()
                .name("Test Phone")
                .description("E2E test product")
                .price(BigDecimal.valueOf(500))
                .stockQuantity(10)
                .categoryId(savedCat.getId())
                .build();
        ProductDTO savedProduct = productService.addProduct(productDTO);
        assertThat(savedProduct.getStockQuantity()).isEqualTo(10);

        // Act: place order (quantity = 3)
        CreateOrderRequestDTO orderRequest = new CreateOrderRequestDTO();
        orderRequest.setUserId(user.getId());
        orderRequest.setItems(List.of(
                new CreateOrderRequestDTO.OrderItemRequest(savedProduct.getId(), 3)));

        OrderDTO placedOrder = orderService.placeOrder(orderRequest);

        // Assert: order is PENDING, stock reduced to 7
        assertThat(placedOrder.getStatus()).isEqualTo(OrderStatus.PENDING);
        assertThat(placedOrder.getUserId()).isEqualTo(user.getId());

        ProductDTO afterOrder = productService.getProductById(savedProduct.getId());
        assertThat(afterOrder.getStockQuantity()).isEqualTo(7);

        // Act: cancel order
        OrderDTO cancelled = orderService.cancelOrder(placedOrder.getId());

        // Assert: status is CANCELLED, stock restored to 10
        assertThat(cancelled.getStatus()).isEqualTo(OrderStatus.CANCELLED);

        ProductDTO afterCancel = productService.getProductById(savedProduct.getId());
        assertThat(afterCancel.getStockQuantity()).isEqualTo(10);
    }

    @Test
    void cancelOrder_shouldFail_whenOrderIsShipped() {
        // Arrange
        UserDTO userDTO = new UserDTO(null, "Ship User", "ship@example.com", "Password@123", Role.USER);
        User user = userService.addUser(userDTO);

        CategoryDTO catDTO = new CategoryDTO();
        catDTO.setName("Gadgets");
        CategoryDTO savedCat = categoryService.addCategory(catDTO);

        ProductDTO productDTO = ProductDTO.builder()
                .name("Gadget")
                .description("A gadget")
                .price(BigDecimal.valueOf(200))
                .stockQuantity(5)
                .categoryId(savedCat.getId())
                .build();
        ProductDTO savedProduct = productService.addProduct(productDTO);

        CreateOrderRequestDTO orderRequest = new CreateOrderRequestDTO();
        orderRequest.setUserId(user.getId());
        orderRequest.setItems(List.of(
                new CreateOrderRequestDTO.OrderItemRequest(savedProduct.getId(), 1)));

        OrderDTO placedOrder = orderService.placeOrder(orderRequest);

        // Manually advance order to SHIPPED
        orderRepository.findById(placedOrder.getId()).ifPresent(o -> {
            o.setStatus(OrderStatus.SHIPPED);
            orderRepository.save(o);
        });

        // Act & Assert: cancellation should throw
        assertThatThrownBy(() -> orderService.cancelOrder(placedOrder.getId()))
                .isInstanceOf(IllegalStateException.class)
                .hasMessageContaining("Cannot cancel order in status: SHIPPED");
    }
}
