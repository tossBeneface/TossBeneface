package com.app.api.logout.service;

import com.app.domain.member.entity.Member;
import com.app.domain.member.service.MemberService;
import com.app.global.error.ErrorCode;
import com.app.global.error.exception.AuthenticationException;
import com.app.global.jwt.constant.TokenType;
import com.app.global.jwt.service.CookieService;
import com.app.global.util.JwtUtils;
import io.jsonwebtoken.Claims;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;

@Service
@Transactional
@RequiredArgsConstructor
public class LogoutService {

    private final MemberService memberService;
    private final CookieService cookieService;
    private final JwtUtils jwtUtils;

    public void logout(String accessToken, HttpServletResponse response) {
        // AccessToken 검증
        jwtUtils.validateToken(accessToken);

        // 토큰 클레임 추출
        Claims tokenClaims = jwtUtils.getTokenClaims(accessToken);
        String tokenType = tokenClaims.getSubject();

        // AccessToken이 아닌 경우 예외 처리
        if(!TokenType.isAccessToken(tokenType)) {
            throw new AuthenticationException(ErrorCode.NOT_ACCESS_TOKEN_TYPE);
        }

        // 사용자 ID로 회원 조회
        Long memberId = Long.valueOf((Integer) tokenClaims.get("memberId"));
        Member member = memberService.findMemberById(memberId);

        // RefreshToken 만료 처리
        member.expireRefreshToken(LocalDateTime.now());

        // RefreshToken 쿠키 삭제
        cookieService.removeCookie(response, "refreshToken");
    }
}
