package com.app.api.payment.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@AllArgsConstructor
@Getter
@Builder
@Setter
public class PaymentDto {
    private String impuid;
    private String name;
    private String status;
    private Long amount;
}
