package com.app.global.jwt.service;

import com.app.global.util.CookieEncryptionUtils;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseCookie;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class CookieService {

    // HttpOnly 쿠키 생성 메서드
    public void addHttpOnlyCookie(HttpServletResponse response, String name, String value, long expirationTime) throws Exception {
        // 쿠키에 저장할 값을 암호화
        String encryptedValue = CookieEncryptionUtils.encrypt(value);

        ResponseCookie refreshTokenCookie = ResponseCookie.from(name, encryptedValue)
                .httpOnly(true)    // JavaScript에서 접근 불가능
                .secure(true)
                .sameSite("None")  // 크로스 사이트 요청을 허용하려면 "None"
                .path("/")         // 도메인 전체에 적용
                .maxAge(expirationTime) // 쿠키 만료 시간 설정 (초 단위)
                .build();

        response.addHeader("Set-Cookie", refreshTokenCookie.toString());
    }

    // 쿠키 삭제 메서드
    public void removeCookie(HttpServletResponse response, String name) {
        ResponseCookie refreshTokenCookie = ResponseCookie.from(name, "")
                .httpOnly(true)    // JavaScript에서 접근 불가능
                .secure(true)
                .sameSite("None")  // 크로스 사이트 요청을 허용하려면 "None"
                .path("/")         // 도메인 전체에 적용
                .maxAge(0)         // 즉시 만료 (삭제)
                .build();

        response.addHeader("Set-Cookie", refreshTokenCookie.toString());
    }
}
