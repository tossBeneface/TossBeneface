package com.app.api.logout.service;

import com.app.domain.member.entity.Member;
import com.app.domain.member.service.MemberService;
import com.app.global.error.ErrorCode;
import com.app.global.error.exception.AuthenticationException;
import com.app.global.jwt.constant.TokenType;
import com.app.global.jwt.service.TokenManager;
import io.jsonwebtoken.Claims;
import java.time.LocalDateTime;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
@RequiredArgsConstructor
public class LogoutService {

    private final MemberService memberService;
    private final TokenManager tokenManager;

    public void logout(String accessToken) {

        tokenManager.validateToken(accessToken);
        Claims tokenClaims = tokenManager.getTokenClaims(accessToken);
        String tokenType = tokenClaims.getSubject();
        if(!TokenType.isAccessToken(tokenType)) {
            throw new AuthenticationException(ErrorCode.NOT_ACCESS_TOKEN_TYPE);
        }

        Long memberId = Long.valueOf((Integer) tokenClaims.get("memberId"));
        Member member = memberService.findMemberById(memberId);
        member.expireRefreshToken(LocalDateTime.now());

    }
}
