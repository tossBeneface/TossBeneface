package com.app.api.order.controller;

import com.app.api.order.dto.OrderRequest;
import com.app.api.order.service.OrderService;
import com.app.domain.order.entity.OrderItem;
import com.app.domain.order.entity.OrderPayment;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/api/orders")
@CrossOrigin(origins = "http://localhost:3000")
public class OrderController {

    private final OrderService orderService;  // Service를 주입받음

    public OrderController(OrderService orderService) {
        this.orderService = orderService;
    }

    @PostMapping
    public ResponseEntity<Map<String, Object>> createOrder(@RequestBody OrderRequest request) {
        System.out.println("Received order: " + request);

        // 요청 데이터 확인
        if (request.getItems() != null) {
            for (OrderItem item : request.getItems()) {
                System.out.println("Item: " + item.getName() + ", Price: " + item.getPrice() + ", Count: " + item.getCount());
            }
        }

        // OrderRequest를 Order 엔티티로 변환
        OrderPayment orderPayment = new OrderPayment();
        orderPayment.setItems(request.getItems()); // Order에 OrderItem 목록 설정
        // 추가적으로 필요한 필드가 있으면 설정 (예: totalAmount, userId 등)

        // Order 저장
        OrderPayment savedOrderPayment = orderService.saveOrder(orderPayment);

        Map<String, Object> response = new HashMap<>();
        response.put("orderId", savedOrderPayment.getId());  // 저장된 주문의 ID 반환
        response.put("message", "주문이 성공적으로 접수되었습니다.");

        return ResponseEntity.ok(response);
    }
}

