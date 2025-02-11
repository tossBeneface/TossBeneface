package com.app.api.order.controller;

import com.app.api.order.dto.OrderRequest;
import com.app.api.order.repository.OrderItemRepository;
import com.app.api.order.repository.OrderRepository;
import com.app.domain.member.entity.Member;
import com.app.domain.member.repository.MemberRepository;
import com.app.domain.order.entity.OrderItem;
import com.app.domain.order.entity.OrderPayment;
import com.app.global.resolver.memberInfo.MemberInfo;
import com.app.global.resolver.memberInfo.MemberInfoDto;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/orders")
@CrossOrigin(origins = {"http://localhost:3000", "http://localhost:3001"})
public class OrderController {

    private final OrderRepository orderPaymentRepository;  // OrderRepository를 주입받음
    private final MemberRepository memberRepository;  // MemberRepository 주입
    private final OrderItemRepository orderItemRepository;

    public OrderController(OrderRepository orderPaymentRepository, MemberRepository memberRepository, OrderItemRepository orderItemRepository) {
        this.orderPaymentRepository = orderPaymentRepository;
        this.memberRepository = memberRepository;
        this.orderItemRepository = orderItemRepository;
    }

    @PostMapping
    public ResponseEntity<Map<String, Object>> createOrder(@RequestBody OrderRequest request, @MemberInfo MemberInfoDto memberInfoDto) {
        System.out.println("Received order: " + request);

        // 요청 데이터 확인
        if (request.getItems() != null) {
            for (OrderItem item : request.getItems()) {
                System.out.println("Item: " + item.getName() + ", Price: " + item.getPrice() + ", Count: " + item.getCount());
            }
        }

        // OrderRequest를 OrderPayment 엔티티로 변환
        OrderPayment orderPayment = new OrderPayment();
        orderPayment.setItems(request.getItems());  // Order에 OrderItem 목록 설정

        // totalAmount를 프론트에서 받은 값으로 설정
        orderPayment.setTotalAmount(request.getTotalAmount());

        // memberId를 Member 객체로 변환하여 설정
        Member member = memberRepository.findById(memberInfoDto.getMemberId())
                .orElseThrow(() -> new RuntimeException("Member not found"));
        orderPayment.setMember(member);  // OrderPayment에 Member 설정

        // Order 저장 (OrderRepository 사용)
        OrderPayment savedOrderPayment = orderPaymentRepository.save(orderPayment);

        // OrderItem의 orderPayment 설정
        for (OrderItem item : savedOrderPayment.getItems()) {
            item.setOrderPayment(savedOrderPayment);  // OrderPayment 연결
        }

        // OrderItem 저장
        orderItemRepository.saveAll(savedOrderPayment.getItems());  // OrderItem을 개별적으로 저장

        // member의 budget을 totalAmount만큼 차감
        if (Integer.parseInt(member.getBudget()) >= orderPayment.getTotalAmount()) {
            member.setBudget(String.valueOf(Integer.parseInt(member.getBudget()) - orderPayment.getTotalAmount()));  // budget 차감
            System.out.println("빠지는 가격: " + orderPayment.getTotalAmount());
            memberRepository.save(member);  // 변경된 member 정보 저장
        } else {
            return ResponseEntity.status(400).body(Map.of("message", "예산이 부족합니다."));
        }

        Map<String, Object> response = new HashMap<>();
        response.put("orderId", savedOrderPayment.getId());  // 저장된 주문의 ID 반환
        response.put("message", "주문이 성공적으로 접수되었습니다.");

        return ResponseEntity.ok(response);
    }

    @GetMapping("/order-list")
    public ResponseEntity<Map<String, Object>> getMyOrders(@MemberInfo MemberInfoDto memberInfoDto) {
        // 현재 로그인한 사용자 정보 가져오기
        Member member = memberRepository.findById(memberInfoDto.getMemberId())
                .orElseThrow(() -> new RuntimeException("Member not found"));

        // 사용자의 주문 목록 조회
        List<OrderPayment> orders = orderPaymentRepository.findByMember(member);

        // 응답 데이터 생성
        List<Map<String, Object>> orderList = new ArrayList<>();
        for (OrderPayment order : orders) {
            Map<String, Object> orderData = new HashMap<>();
            orderData.put("orderId", order.getId());
            orderData.put("totalAmount", order.getTotalAmount());
            orderData.put("items", order.getItems().stream().map(item -> {
                Map<String, Object> itemData = new HashMap<>();
                itemData.put("name", item.getName());
                itemData.put("price", item.getPrice());
                itemData.put("count", item.getCount());
                return itemData;
            }).toList());
            orderList.add(orderData);
        }

        Map<String, Object> response = new HashMap<>();
        response.put("orders", orderList);
        return ResponseEntity.ok(response);
    }

}
