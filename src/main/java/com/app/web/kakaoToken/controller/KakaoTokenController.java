package com.app.web.kakaoToken.controller;

import com.app.web.kakaoToken.client.KakaoTokenClient;
import com.app.web.kakaoToken.dto.KakaoTokenDto;
import com.app.web.kakaoToken.dto.KakaoTokenDto.Request;
import com.app.web.kakaoToken.dto.KakaoTokenDto.Response;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
@RequiredArgsConstructor
public class KakaoTokenController {

    private final KakaoTokenClient kakaoTokenClient;

    @Value("${kakao.client.id}")
    private String clientId;

    @Value("${kakao.client.secret}")
    private String clientSecret;

    @GetMapping("/login")
    public String login() {
        return "loginForm";
    }

    // redirect uri를 처리할 메서드
    @GetMapping("/oauth/kakao/callback")
    public @ResponseBody String loginCallback(String code) {
        String contentType = "application/x-www-form-urlencoded;charset=utf-8";
        KakaoTokenDto.Request kakaoTokenRequestDto = KakaoTokenDto.Request.builder()
            .client_id(clientId)
            .client_secret(clientSecret)
            .grant_type("authorization_code")
            .code(code)
            .redirect_uri("http://localhost:8080/oauth/kakao/callback")
            .build();
        KakaoTokenDto.Response kakaoToken = kakaoTokenClient.requestKakaoToken(contentType, kakaoTokenRequestDto);
        return "kakao token : " + kakaoToken;
    }

}
