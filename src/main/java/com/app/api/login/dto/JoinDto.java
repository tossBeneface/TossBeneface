package com.app.api.login.dto;

import com.app.global.jwt.dto.JwtTokenDto;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonFormat.Shape;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;

import java.util.Date;

public class JoinDto {

    @Getter @Setter
    public static class Request {

        @Schema(description = "이메일", example = "a061283@aivle.kt.co.kr", required = true)
        private String email;

        // TODO 비밀번호 정책 정하기
        @Schema(description = "비밀번호", example = "비밀번호 정책 설명", required = true)
        private String password;

        @Schema(description = "이름", example = "김에쁠", required = true)
        private String memberName;

        @Schema(description = "휴대폰 번호", example = "010-0000-0000", required = true)
        private String phoneNumber;

        // 토스 회원가입시X, 필요데이터
        @Schema(description = "프로필 사진", example = "없을시 기본 이미지로 대체", required = false)
        private String profileImg;

    }

    @Getter @Setter
    @Builder @NoArgsConstructor @AllArgsConstructor
    public static class Response {

        // 회원가입 성공에 따른 id부여 : 서비스에서 넣어주기
        @Schema(description = "MemberId", example = "1", required = true)
        private String memberId;

        // 회원가입 직후 바로 프로필 정보를 보여주기 위한 정보들
        @Schema(description = "이메일", example = "a061283@aivle.kt.co.kr", required = true)
        private String email;

        @Schema(description = "이름", example = "1", required = true)
        private String memberName;

        @Schema(description = "휴대폰 번호", example = "010-0000-0000", required = true)
        private String phoneNumber;

        // TODO 서비스에서
        @Schema(description = "프로필 사진", example = "없을시 기본 이미지로 대체", required = false)
        private String profileImg;

        @Schema(description = "일반 사용자/관리자 여부", example = "관리자", required = true)
        private String role;

        @Schema(description = "grantType", example = "Bearer", required = true)
        private String grantType;

        // 회원 가입 직후 로그인 처리
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

        public static Response of(JwtTokenDto jwtTokenDto, JoinDto.Request request) {
            return Response.builder()
                    .email(request.getEmail())
                    .memberName(request.getMemberName())
                .grantType(jwtTokenDto.getGrantType())
                .accessToken(jwtTokenDto.getAccessToken())
                .accessTokenExpireTime(jwtTokenDto.getAccessTokenExpireTime())
                .refreshToken(jwtTokenDto.getRefreshToken())
                .refreshTokenExpireTime(jwtTokenDto.getRefreshTokenExpireTime())
                .build();
        }
    }
}
