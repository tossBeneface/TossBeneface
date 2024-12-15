package com.app.api.login.service;

import com.app.api.login.dto.OauthLoginDto;
import com.app.domain.member.constant.MemberType;
import com.app.domain.member.constant.Role;
import com.app.domain.member.entity.Member;
import com.app.domain.member.service.MemberService;
import com.app.external.oauth.model.OAuthAttributes;
import com.app.external.oauth.service.SocialLoginApiService;
import com.app.external.oauth.service.SocialLoginApiServiceFactory;
import com.app.global.jwt.dto.JwtTokenDto;
import com.app.global.jwt.service.TokenManager;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Slf4j
@Transactional
@RequiredArgsConstructor
public class OauthLoginService {

    private final MemberService memberService;
    private final TokenManager tokenManager;

    public OauthLoginDto.Response oauthLogin(String accessToken, MemberType memberType) {
        SocialLoginApiService socialLoginApiService = SocialLoginApiServiceFactory.getSocialLoginApiService(memberType);
        OAuthAttributes userInfo = socialLoginApiService.getUserInfo(accessToken);
        log.info("userInfo : {}", userInfo);

        // 회원 가입 처리 진행
        JwtTokenDto jwtTokenDto;
        // 해당 이메일로 회원이 있는지부터 확인
        Optional<Member> optionalMember = memberService.findMemberByEmail(userInfo.getEmail());
        if (optionalMember.isEmpty()) { // 신규 회원 가입
            Member oauthMember = userInfo.toMemberEntity(memberType, Role.ADMIN);
            // 인가 테스트
//            Member oauthMember = userInfo.toMemberEntity(memberType, Role.USER);

            // 어차피 저장 돼야 진짜니까 새로 참조시키나 보다
            oauthMember = memberService.registerMember(oauthMember);
            jwtTokenDto = tokenManager.createJwtTokenDto(oauthMember.getMemberId(), oauthMember.getRole());
            oauthMember.updateRefreshToken(jwtTokenDto);

            // 토큰 생성
        } else { // 기존 회원일 경우 토큰발급만 해주기
            Member oauthMember = optionalMember.get();
            // 토큰 생성
            jwtTokenDto = tokenManager.createJwtTokenDto(oauthMember.getMemberId(), oauthMember.getRole());
            oauthMember.updateRefreshToken(jwtTokenDto);
        }

        return OauthLoginDto.Response.of(jwtTokenDto);

    }
}
