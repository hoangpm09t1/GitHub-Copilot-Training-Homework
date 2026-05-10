package com.example.copilot.service.repository;

import com.example.copilot.core.entity.Order;
import com.example.copilot.core.enums.OrderStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface OrderRepository extends JpaRepository<Order, Long> {

    List<Order> findByUserId(Long userId);

    List<Order> findByStatusAndOrderDateBefore(OrderStatus status, LocalDateTime dateTime);
}
