package com.example.copilot.api.scheduler;

import com.example.copilot.core.entity.Order;
import com.example.copilot.core.enums.OrderStatus;
import com.example.copilot.service.repository.OrderRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

@Component
@RequiredArgsConstructor
@Slf4j
public class OrderScheduler {

    private final OrderRepository orderRepository;

    @Scheduled(fixedRate = 3600000)
    @Transactional
    public void cancelStalePendingOrders() {
        LocalDateTime cutoff = LocalDateTime.now().minusHours(24);
        List<Order> staleOrders = orderRepository.findByStatusAndOrderDateBefore(OrderStatus.PENDING, cutoff);
        staleOrders.forEach(order -> order.setStatus(OrderStatus.CANCELLED));
        orderRepository.saveAll(staleOrders);
        if (!staleOrders.isEmpty()) {
            log.info("Cancelled {} stale PENDING orders older than 24 hours", staleOrders.size());
        }
    }
}
