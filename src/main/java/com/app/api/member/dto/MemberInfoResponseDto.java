package com.app.api.member.dto;

import com.app.domain.member.entity.Member;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.app.global.util.DateTimeUtils;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;
import lombok.Getter;

import java.util.Date;

@Getter
@Builder
public class MemberInfoResponseDto {

    @Schema(description = "MemberId", example = "1", required = true)
    private String memberId;

    @Schema(description = "이메일", example = "a061283@aivle.kt.co.kr", required = true)
    private String email;

    @Schema(description = "이름", example = "1", required = true)
    private String memberName;

    @Schema(description = "휴대폰 번호", example = "010-0000-0000", required = true)
    private String phoneNumber;

    @Schema(description = "성별", example = "male/female", required = true)
    private String gender;

    @Schema(description = "잔액", example = "10000원", required = true)
    private String budget;

    @Schema(description = "프로필 사진", example = "없을시 기본 이미지로 대체", required = false)
    private String profileImg;

    @Schema(description = "일반 사용자/관리자 여부", example = "관리자", required = true)
    private String role;

    @Schema(description = "refreshToken", example = "eyJ0eXBlIjoiSldUIiwiYWxnIjoiSFM1MTIifQ.eyJzdWIiOiJSRUZSRVNIIiwiaWF0IjoxNzExMjAyNTk0LCJleHAiOjE3MTI0MTIxOTQsIm1lbWJlcklkIjoxfQ.oZevzw1fV-UqMlt_nXIOb0yE36cuesWOYSQE4vA8OtvTebXZ92veYNoZXQroTg-v3Rzg_9Jl85OmE2tkCrtMrw")
    private String refreshToken;

    @Schema(description = "refresh token 만료 시간", example = "2024-04-06 23:03:14", required = true)
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss", timezone = "Asia/Seoul")
    private Date refreshTokenExpireTime;


    public static MemberInfoResponseDto of(Member member) {
        return MemberInfoResponseDto.builder()
            .memberId(String.valueOf(member.getMemberId()))
            .memberName(member.getMemberName())
            .email(member.getEmail())
            .phoneNumber(member.getPhoneNumber())
            .gender(String.valueOf(member.getGender()))
            .budget(member.getBudget())
            .profileImg(member.getProfileImg())
            .role(String.valueOf(member.getRole()))
            .refreshToken(member.getRefreshToken())
            .refreshTokenExpireTime(DateTimeUtils.convertToDate(member.getTokenExpirationTime()))
            .build();
    }
}
