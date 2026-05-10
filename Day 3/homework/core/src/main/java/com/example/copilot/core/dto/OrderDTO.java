package com.example.copilot.core.dto;

import com.example.copilot.core.enums.OrderStatus;
import lombok.*;

import java.time.LocalDateTime;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class OrderDTO {

    private Long id;
    private LocalDateTime orderDate;
    private OrderStatus status;
    private Long userId;
    private List<OrderItemDTO> orderItems;
}
