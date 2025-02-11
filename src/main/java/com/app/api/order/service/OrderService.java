package com.app.api.order.service;

import com.app.api.order.repository.OrderRepository;
import com.app.domain.order.entity.OrderPayment;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor  // 생성자 주입을 자동으로 생성
public class OrderService {

    private final OrderRepository orderRepository;  // OrderRepository가 자동으로 주입됩니다.

    public OrderPayment saveOrder(OrderPayment order) {
        orderRepository.save(order);
        return order;
    }
}

