package com.app.global.jwt.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonFormat.Shape;
import lombok.*;

import java.util.Date;

@Getter
@Setter
@Builder
@AllArgsConstructor @NoArgsConstructor
public class JwtTokenDto {

    private String memberId;
    private String grantType;
    private String accessToken;
    @JsonFormat(shape = Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss", timezone = "Asia/Seoul") // 타임존 한국시간에 맞추기 위해 사용
    private Date accessTokenExpireTime;
    private String refreshToken;
    @JsonFormat(shape = Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss", timezone = "Asia/Seoul")
    private Date refreshTokenExpireTime;

}
