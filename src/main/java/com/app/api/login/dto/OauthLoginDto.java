package com.app.api.login.dto;

import com.app.global.jwt.dto.JwtTokenDto;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonFormat.Shape;
import io.swagger.v3.oas.annotations.media.Schema;
import java.util.Date;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.web.bind.annotation.GetMapping;

public class OauthLoginDto {

    @Getter @Setter
    public static class Request {
        @Schema(description = "소셜 로그인 회원 타입", example = "KAKAO", required = true)
        private String memberType;
    }

    @Getter @Setter
    @Builder @NoArgsConstructor @AllArgsConstructor
    public static class Response {

        @Schema(description = "grantType", example = "Bearer", required = true)
        private String grantType;

        @Schema(description = "accessToken", example = "eyJ0eXBlIjoiSldUIiwiYWxnIjoiSFM1MTIifQ.eyJzdWIiOiJBQ0NFU1MiLCJpYXQiOjE3MTEyMDI1OTQsImV4cCI6MTcxMTIwMzQ5NCwibWVtYmVySWQiOjEsInJvbGUiOiJBRE1JTiJ9.DJxNS5CQes5Q2_L11N0Fxo_yPc06s8HEtZdDiRQBJzZDXNHfu5o699_4NfLk8K_xrqPtE54SWNltZ68rl77_6w", required = true)
        private String accessToken;

        @Schema(description = "access token 만료 시간", example = "2024-03-23 23:18:14", required = true)
        @JsonFormat(shape = Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss", timezone = "Asia/Seoul")
        private Date accessTokenExpireTime;

        @Schema(description = "refreshToken", example = "eyJ0eXBlIjoiSldUIiwiYWxnIjoiSFM1MTIifQ.eyJzdWIiOiJSRUZSRVNIIiwiaWF0IjoxNzExMjAyNTk0LCJleHAiOjE3MTI0MTIxOTQsIm1lbWJlcklkIjoxfQ.oZevzw1fV-UqMlt_nXIOb0yE36cuesWOYSQE4vA8OtvTebXZ92veYNoZXQroTg-v3Rzg_9Jl85OmE2tkCrtMrw")
        private String refreshToken;

        @Schema(description = "refresh token 만료 시간", example = "2024-04-06 23:03:14", required = true)
        @JsonFormat(shape = Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss", timezone = "Asia/Seoul")
        private Date refreshTokenExpireTime;

        public static Response of(JwtTokenDto jwtTokenDto) {
            return Response.builder()
                .grantType(jwtTokenDto.getGrantType())
                .accessToken(jwtTokenDto.getAccessToken())
                .accessTokenExpireTime(jwtTokenDto.getAccessTokenExpireTime())
                .refreshToken(jwtTokenDto.getRefreshToken())
                .refreshTokenExpireTime(jwtTokenDto.getRefreshTokenExpireTime())
                .build();
        }
    }
}
