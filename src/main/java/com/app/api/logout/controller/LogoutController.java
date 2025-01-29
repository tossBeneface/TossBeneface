package com.app.api.logout.controller;

import com.app.api.logout.service.LogoutService;
import com.app.global.util.AuthorizationHeaderUtils;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Tag(name = "authentication", description = "로그인/로그아웃/토큰재발급 API")
@RestController
@RequiredArgsConstructor
@RequestMapping("/api")
public class LogoutController {

    private final LogoutService logoutService;

    @Tag(name = "authentication")
    @Operation(summary = "로그아웃 API", description = "로그아웃 시 refresh token 만료 처리")
    @PostMapping("/logout")
    public ResponseEntity<String> logout(HttpServletRequest httpServletRequest, HttpServletResponse response) {
        String authorizationHeader = httpServletRequest.getHeader("Authorization");
        AuthorizationHeaderUtils.validateAuthorization(authorizationHeader);
        String accessToken = authorizationHeader.split(" ")[1];

        logoutService.logout(accessToken, response);

        // 204 No Content를 사용하는 것이 RESTful API 설계에 부합
        return ResponseEntity.noContent().build();
    }
}
