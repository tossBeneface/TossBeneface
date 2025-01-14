package com.app.domain.member.entity;

import com.app.domain.common.BaseEntity;
import com.app.domain.member.constant.Role;
import com.app.domain.member.constant.MemberStatus;
import com.app.global.jwt.dto.JwtTokenDto;
import com.app.global.util.DateTimeUtils;
import java.time.LocalDateTime;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Member extends BaseEntity {

    // 토스 가입창 : 성명, 이메일 주소, 전화번호, 비밀번호
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY) // 기본키 생성 전략을 데이터베이스에 맡기겠다
    private Long memberId;

    @Column(unique = true, length = 50, nullable = false)
    private String email;

    @Column(nullable = false, length = 200)
    private String password;

    @Column(nullable = false, length = 20)
    private String memberName;

    @Column(nullable = false, length = 20)
    private String phoneNumber;

    // 토스 회원가입시X, 필요데이터
    @Column(nullable = true, length = 200)
    private String profileImg;

    @Column(nullable = false, length = 200)
    private String budget;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 10)
    private Role role;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 200)
    private MemberStatus memberStatus;

    @Column(length = 250)
    private String refreshToken;

    private LocalDateTime tokenExpirationTime;

    // 필요한 필드들만 빌더로 받아서 사용할 수 있게
    @Builder
    public Member(String email, String password, String memberName, String phoneNumber,
        String profileImg, String budget, Role role, MemberStatus memberStatus) {

        this.email = email;
        this.password = password;
        this.memberName = memberName;
        this.phoneNumber = phoneNumber;
        this.profileImg = profileImg;
        this.budget = budget;
        this.role = role;
        this.memberStatus = memberStatus;
    }

    public void updateRefreshToken(JwtTokenDto jwtTokenDto) {
        this.refreshToken = jwtTokenDto.getRefreshToken();
        this.tokenExpirationTime = DateTimeUtils.convertToLocalDateTime(jwtTokenDto.getRefreshTokenExpireTime());
    }

    public void expireRefreshToken(LocalDateTime now) {
        this.tokenExpirationTime = now;
    }
}
