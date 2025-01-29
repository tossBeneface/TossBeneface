package com.app.global.jwt.service;

import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class CookieService {

    // HttpOnly 쿠키 생성 메서드
    public void addHttpOnlyCookie(HttpServletResponse response, String name, String value, long expirationTime) {
        Cookie cookie = new Cookie(name, value);
        cookie.setHttpOnly(true); // JavaScript 접근 방지
        cookie.setSecure(true);  // HTTPS 전용 설정
        cookie.setPath("/");     // 도메인 전체에 적용
        cookie.setMaxAge((int) expirationTime); // ms -> s 변환 // ms -> s 변환
        response.addCookie(cookie);
    }

    // 쿠키 삭제 메서드
    public void removeCookie(HttpServletResponse response, String name) {
        Cookie cookie = new Cookie(name, null);
        cookie.setHttpOnly(true);
        cookie.setSecure(true);
        cookie.setPath("/");
        cookie.setMaxAge(0); // 즉시 만료
        response.addCookie(cookie);
    }
}
