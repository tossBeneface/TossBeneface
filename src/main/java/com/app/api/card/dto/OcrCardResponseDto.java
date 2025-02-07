package com.app.api.card.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class OcrCardResponseDto {
    private boolean cardDetected;
    private String cardNumber;
    private String cardName;
    private String cardCompany;
    private String cardImage;
    private String dateInfo;
    private String cvcInfo;
    private String otherText;
}