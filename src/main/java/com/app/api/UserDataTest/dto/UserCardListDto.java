package com.app.api.UserDataTest.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class UserCardListDto {
    private String cardName;
    private String cardCompany;
    private String cardImage;
    private int accrueBenefit;
}