package com.app.api.login.controller;

import com.app.api.login.dto.OauthLoginDto;
import com.app.api.login.service.OauthLoginService;
import com.app.api.login.validator.OauthValidator;
import com.app.domain.member.constant.MemberType;
import com.app.global.util.AuthorizationHeaderUtils;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import javax.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Tag(name = "authentication", description = "로그인/로그아웃/토큰재발급 API")
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/oauth")
public class OAuthLoginController {

    private final OauthValidator oauthValidator;
    private final OauthLoginService oauthLoginService;

    @Tag(name = "authentication")
    @Operation(summary = "소셜 로그인 API", description = "소셜 로그인 API")
    @PostMapping("/login")
    public ResponseEntity<OauthLoginDto.Response> oauthLogin(@RequestBody OauthLoginDto.Request oauthLoginRequestDto,
        HttpServletRequest httpServletRequest) {

        // 인증 관련 정보니까 Authorization에 담아서 보내줄거다..?
        String authorizationHeader = httpServletRequest.getHeader("Authorization");

        // 사용자가 입력 안하고 보낼수도 있으니 필수값 체크를 하겠다.
        AuthorizationHeaderUtils.validateAuthorization(authorizationHeader);
        // 제대로된 멤버타입인지 확인
        oauthValidator.validateMemberType(oauthLoginRequestDto.getMemberType());

        // 로그인 로직(서비스) 실행
        String accessToken = authorizationHeader.split(" ")[1];
        OauthLoginDto.Response jwtTokenResponseDto = oauthLoginService
            .oauthLogin(accessToken, MemberType.from(oauthLoginRequestDto.getMemberType()));

        return ResponseEntity.ok(jwtTokenResponseDto);
    }
}
