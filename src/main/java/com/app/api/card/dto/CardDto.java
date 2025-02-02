package com.app.api.card.dto;

import lombok.Getter;

@Getter
public class CardDto {
    private String cardNumber;
    private String cardType;
    private String expiryDate;
    private String customerKey;
    private Long memberId;

    // Getter & Setter
}
