package com.app.api.login.controller;

import com.app.api.login.dto.JoinDto;
import com.app.api.login.dto.LoginDto;
import com.app.api.login.service.LoginService;
import com.app.global.jwt.service.CookieService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@Tag(name = "authentication", description = "로그인/로그아웃/토큰재발급 API")
@RestController
@CrossOrigin(origins = {"http://localhost:3001", "http://localhost:3000"}, allowedHeaders = "*", methods = { RequestMethod.GET, RequestMethod.POST, RequestMethod.OPTIONS })
@RequiredArgsConstructor
@RequestMapping("/api")
@Slf4j
public class LoginController {

    private final LoginService loginService;
    private final CookieService cookieService;

    @Tag(name = "authentication")
    @Operation(summary = "회원가입 API", description = "회원가입 API")
    @PostMapping("/join")
    public ResponseEntity<JoinDto.Response> join(@RequestBody JoinDto.Request joinRequestDto,
                                                        HttpServletResponse httpServletResponse) {
        log.info("회원가입 요청 수신: {}", joinRequestDto);

        JoinDto.Response joinResponseDto = loginService.join(joinRequestDto, httpServletResponse);

        return ResponseEntity.ok(joinResponseDto);
    }

    @Tag(name = "authentication")
    @Operation(summary = "로그인 API", description = "로그인 API")
    @PostMapping("/login")
    public ResponseEntity<LoginDto.Response> login(@RequestBody LoginDto.Request loginRequestDto, HttpServletResponse response) {

        LoginDto.Response jwtTokenResponseDto = loginService.login(loginRequestDto, response);
        return ResponseEntity.ok(jwtTokenResponseDto);
    }
}
