package com.app.domain.order.entity;

import com.app.domain.member.entity.Member;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Entity
@Getter
@Setter
public class OrderItem {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;
    private int price;
    private int count;

    @ManyToOne
    @JoinColumn(name = "order_payment_id")
    private OrderPayment orderPayment;

    // Getters and Setters
}

