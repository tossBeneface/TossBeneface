package com.app.api.token.controller;

import com.app.api.token.dto.AccessTokenResponseDto;
import com.app.api.token.service.TokenService;
import com.app.global.error.ErrorCode;
import com.app.global.error.exception.AuthenticationException;
import com.app.global.util.CookieEncryptionUtils;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Arrays;

@Tag(name = "authentication", description = "로그인/로그아웃/토큰재발급 API")
@RestController
@RequiredArgsConstructor
@RequestMapping("/api")
@Slf4j
public class TokenController {

    private final TokenService tokenService;

    @Tag(name = "authentication")
    @Operation(summary = "Access Token 재발급 API", description = "Access Token 재발급 API")
    @PostMapping("/access-token/issue")
    public ResponseEntity<AccessTokenResponseDto> createAccessToken(HttpServletRequest httpServletRequest, HttpServletResponse response) throws Exception {
        // 쿠키에서 RefreshToken 추출
        Cookie[] cookies = httpServletRequest.getCookies();
        if (cookies == null) {
            log.error("No cookies found in the request");
            throw new AuthenticationException(ErrorCode.REFRESH_TOKEN_NOT_FOUND);
        }

        String encryptedRefreshToken = Arrays.stream(cookies)
                .filter(cookie -> "refreshToken".equals(cookie.getName()))
                .map(Cookie::getValue)
                .findFirst()
                .orElseThrow(() -> new AuthenticationException(ErrorCode.REFRESH_TOKEN_NOT_FOUND));

        // 복호화 처리
        String refreshToken;
        try {
            refreshToken = CookieEncryptionUtils.decrypt(encryptedRefreshToken);
        } catch (Exception e) {
            log.error("Failed to decrypt refresh token", e);
            throw new AuthenticationException(ErrorCode.REFRESH_TOKEN_NOT_FOUND);
        }

        // 복호화된 refresh token을 사용해 AccessToken 생성
        AccessTokenResponseDto accessTokenResponseDto = tokenService.createAccessTokenByRefreshToken(refreshToken, response);

        return ResponseEntity.ok(accessTokenResponseDto);
    }


}
