package com.app.api.card.dto;

import lombok.Getter;
import lombok.Setter;
import org.springframework.web.multipart.MultipartFile;

@Getter
@Setter
public class OcrCardRequestDto {
    private MultipartFile image; // 이미지 파일
}