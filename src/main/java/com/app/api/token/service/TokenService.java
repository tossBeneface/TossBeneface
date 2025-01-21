package com.app.api.token.service;

import com.app.api.token.dto.AccessTokenResponseDto;
import com.app.domain.member.entity.Member;
import com.app.domain.member.service.MemberService;
import com.app.global.jwt.constant.GrantType;
import com.app.global.jwt.dto.JwtTokenDto;
import com.app.global.jwt.service.CookieService;
import com.app.global.jwt.service.TokenManager;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
@RequiredArgsConstructor
public class TokenService {

    private final MemberService memberService;
    private final TokenManager tokenManager;
    private final CookieService cookieService;

    public AccessTokenResponseDto createAccessTokenByRefreshToken(String refreshToken, HttpServletResponse response) {

        Member member = memberService.findMemberByRefreshToken(refreshToken);
        tokenManager.validateToken(refreshToken);

        // 2. 새로운 AccessToken 및 RefreshToken 생성
        JwtTokenDto jwtTokenDto = tokenManager.createJwtTokenDto(member.getMemberId(), member.getRole());
        member.updateRefreshToken(jwtTokenDto); // 사용자 엔터티에 새로운 RefreshToken 저장

        // 3. RefreshToken 갱신 여부에 따라 쿠키 저장
        cookieService.addHttpOnlyCookie(response, "refreshToken", jwtTokenDto.getRefreshToken());

        return AccessTokenResponseDto.builder()
            .grantType(GrantType.BEARER.getType())
            .accessToken(jwtTokenDto.getAccessToken())
            .accessTokenExpireTime(jwtTokenDto.getAccessTokenExpireTime())
            .build();
    }
}
