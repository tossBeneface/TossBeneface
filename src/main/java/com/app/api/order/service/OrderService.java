package com.app.api.order.service;

import com.app.api.order.repository.OrderRepository;
import com.app.domain.order.entity.OrderPayment;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class OrderService {

    private OrderRepository orderRepository;
    // 생성자 주입
    public OrderService(OrderRepository orderRepository) {
        this.orderRepository = orderRepository;
    }

    public OrderPayment saveOrder(OrderPayment orderPayment) {
        return orderRepository.save(orderPayment); // 주문을 데이터베이스에 저장
    }
}

