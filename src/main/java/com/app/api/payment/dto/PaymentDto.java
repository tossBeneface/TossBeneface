package com.app.api.payment.dto;

import com.app.api.payment.PayType;
import com.app.domain.payment.entity.Payment;
import lombok.*;

import java.util.UUID;

@Getter
@Builder
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class PaymentDto {
    @NonNull
    private PayType payType;
    @NonNull
    private Long amount;
    @NonNull
    private String orderName;
    private String yourSuccessUrl;
    private String yourFailUrl;

    public Payment toEntity() {
        return Payment.builder()
                .payType(payType)
                .amount(amount)
                .orderName(orderName)
                .orderId(UUID.randomUUID().toString())
                .paySuccessYN(false)
                .build();
    }
}
