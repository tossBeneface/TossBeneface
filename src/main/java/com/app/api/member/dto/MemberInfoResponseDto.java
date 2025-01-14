package com.app.api.member.dto;

import com.app.domain.member.constant.Role;
import com.app.domain.member.entity.Member;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class MemberInfoResponseDto {

    @Schema(description = "회원 아이디", example = "1", required = true)
    private Long memberId;

    @Schema(description = "이메일", example = "test@example.com", required = true)
    private String email;

    @Schema(description = "회원 이름", example = "김모모", required = true)
    private String memberName;

    @Schema(description = "프로필 이미지 경로", example = "http://k.kakaocdn.net/example/img_110x110.jpg", required = false)
    private String profile;

    @Schema(description = "회원의 역할", example = "USER", required = true)
    private Role role;

    public static MemberInfoResponseDto of(Member member) {
        return MemberInfoResponseDto.builder()
            .memberId(member.getMemberId())
            .memberName(member.getMemberName())
            .email(member.getEmail())
            .profile(member.getProfileImg())
            .role(member.getRole())
            .build();
    }
}
