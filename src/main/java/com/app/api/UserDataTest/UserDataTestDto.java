package com.app.api.UserDataTest;

import lombok.Getter;
import lombok.Setter;

public class UserDataTestDto {
}



@Getter
public class UserDataTestDto {
    private Long memberId;
    private String cardNumber;
    private int cvc;
    private int pwd;
    private String expiryDate;
}
