package com.app.api.UserDataTest.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class UserCardListDto {
    private Long cardId;
    private String cardName;
    private String cardCompany;
    private String cardImage;
    private int accrueBenefit;
    private String cardNumber;
    private String expiryDate;
    private int cvc;
}