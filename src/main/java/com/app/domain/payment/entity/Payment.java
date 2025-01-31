package com.app.domain.payment.entity;

import com.app.api.payment.PayType;
import com.app.api.payment.dto.PaymentResDto;
import com.app.domain.common.BaseEntity;
import com.app.domain.member.entity.Member;
import jakarta.persistence.*;
import lombok.*;

@Entity
@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Setter
@Table(indexes = {
        @Index(name = "idx_payment_member", columnList = "customer"),
        @Index(name = "idx_payment_paymentKey", columnList = "paymentKey"),
})
public class Payment extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "payment_id", nullable = false, unique = true)
    private Long paymentId;
    @Column(nullable = true, name = "pay_type")
    @Enumerated(EnumType.STRING)
    private PayType payType;
    @Column(nullable = true, name = "pay_amount")
    private Long amount;
    @Column(nullable = true, name = "pay_name")
    private String orderName;
    @Column(nullable = true, name = "order_id")
    private String orderId;

    private boolean paySuccessYN;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "customer")
    private Member customer;
    @Column
    private String paymentKey;
    @Column
    private String failReason;

    @Column
    private boolean cancelYN;
    @Column
    private String cancelReason;

    public PaymentResDto toPaymentResDto() {
        return PaymentResDto.builder()
                .payType(String.valueOf(payType))
                .amount(amount)
                .orderName(orderName)
                .orderId(orderId)
                .customerEmail(customer.getEmail())
                .customerName(customer.getMemberName())
                .cancelYN(cancelYN)
                .failReason(failReason)
                .build();
    }


}