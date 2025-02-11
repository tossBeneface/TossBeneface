package com.app.domain.order.entity;

import com.app.domain.member.entity.Member;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Entity
@Getter
@Setter
public class OrderPayment {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToMany(mappedBy = "orderPayment", cascade = CascadeType.ALL)
    private List<OrderItem> items;

    private Integer totalAmount;

    @ManyToOne
    @JoinColumn(name = "member_id")
    private Member member;

    private String cardReason;
}

