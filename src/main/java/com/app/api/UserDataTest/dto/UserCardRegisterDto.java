package com.app.api.UserDataTest.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class UserCardRegisterDto {
    private String cardName;      // 카드 이름
    private String cardCompany;   // 카드 회사
    private String cardNumber;    // 카드 번호
    private String expiryDate;    // 유효기간
    private int cvc;              // CVC 번호
    private int pwd;              // 카드 비밀번호
}