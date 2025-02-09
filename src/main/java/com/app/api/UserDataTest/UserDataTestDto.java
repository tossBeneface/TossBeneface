package com.app.api.UserDataTest;

import lombok.Getter;
import lombok.Setter;

@Getter
public class UserDataTestDto {
    private Long memberId;
    private String cardNumber;
    private int cvc;
    private int pwd;
    private String expiryDate;
}

//    c.card_name AS cardName,
//    c.card_company AS cardCompany,
//    c.card_image AS cardImage,
//    IFNULL(udt.accrue_benefit, 0) AS amount