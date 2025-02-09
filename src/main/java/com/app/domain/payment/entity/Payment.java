package com.app.domain.payment.entity;

import com.app.domain.member.entity.Member;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Entity
@Setter
@Getter
@Table(name = "payment")
public class Payment {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String paymentKey;
    private String orderId;
    private String orderName;
    private String method;
    private int totalAmount;
    private String status;
    private String requestedAt;
    private String approvedAt;
    private String receiptUrl;
    @ManyToOne
    @JoinColumn(name = "member_id")
    private Member member;

}
