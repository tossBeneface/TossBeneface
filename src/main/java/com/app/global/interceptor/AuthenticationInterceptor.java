package com.app.global.interceptor;

import com.app.global.error.ErrorCode;
import com.app.global.error.exception.AuthenticationException;
import com.app.global.jwt.constant.TokenType;
import com.app.global.util.AuthorizationHeaderUtils;
import com.app.global.util.JwtUtils;
import io.jsonwebtoken.Claims;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

@Component
@RequiredArgsConstructor
@Slf4j
public class AuthenticationInterceptor implements HandlerInterceptor {

    private final JwtUtils jwtUtils;

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler)
        throws Exception {
        log.info("AuthenticationInterceptor 호출: {}", request.getRequestURI());

        // 1. Authorization Header 검증
        String authorizationHeader = request.getHeader("Authorization");
        log.info("Authorization Header: {}", authorizationHeader);
        AuthorizationHeaderUtils.validateAuthorization(authorizationHeader);

        // 2. TOKEN 검증
        String token = authorizationHeader.split(" ")[1];
        jwtUtils.validateToken(token);

        // 3. TOKEN TYPE 검증
        Claims tokenClaims = jwtUtils.getTokenClaims(token);
        String tokenType = tokenClaims.getSubject();
        if (!TokenType.isAccessToken(tokenType)) {
            throw new AuthenticationException(ErrorCode.NOT_ACCESS_TOKEN_TYPE);
        }
        log.debug("Received JWT Token: {}", token);
        log.debug("Parsed Claims: {}", tokenClaims);
        log.info("토큰 유효성 검사 완료");
        return true;
    }
}
