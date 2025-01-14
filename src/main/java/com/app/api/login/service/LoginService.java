package com.app.api.login.service;

import com.app.api.login.dto.JoinDto;
import com.app.api.login.dto.LoginDto;
import com.app.api.login.validator.LoginValidator;
import com.app.domain.member.constant.MemberStatus;
import com.app.domain.member.constant.Role;
import com.app.domain.member.entity.Member;
import com.app.domain.member.repository.MemberRepository;
import com.app.domain.member.service.MemberService;
import com.app.global.error.ErrorCode;
import com.app.global.error.exception.BusinessException;
import com.app.global.jwt.dto.JwtTokenDto;
import com.app.global.jwt.service.TokenManager;

import java.util.Optional;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Slf4j
@Transactional
@RequiredArgsConstructor
public class LoginService {

    private final LoginValidator loginValidator;
    private final MemberService memberService;
    private final TokenManager tokenManager;
    private final BCryptPasswordEncoder bCryptPasswordEncoder;
    private final MemberRepository memberRepository;

    public JoinDto.Response join(JoinDto.Request request) {

        JwtTokenDto jwtTokenDto;

        loginValidator.validateMemberEmail(request.getEmail());

        Member member = Member.builder()
                .email(request.getEmail())
                .password(bCryptPasswordEncoder.encode(request.getPassword()))
                .memberName(request.getMemberName())
                .phoneNumber(request.getPhoneNumber())
                .budget("10000")
                .memberStatus(MemberStatus.ACTIVATE)
                //TODO 기본이미지 처리 -> 프론트에 넘겨서?
                .profileImg(Optional.ofNullable(request.getProfileImg()).orElse(""))
                .build();

        member = memberService.registerMember(member);
        jwtTokenDto = tokenManager.createJwtTokenDto(member.getMemberId(), member.getRole());
        log.info("memberInfo : {}", member);
        member.updateRefreshToken(jwtTokenDto);

        return JoinDto.Response.of(jwtTokenDto, request);

    }

    public LoginDto.Response login(LoginDto.Request request) {
        JwtTokenDto jwtTokenDto;

        loginValidator.validateMemberEmail(request.getEmail());


        Member member = memberRepository.findByEmail(request.getEmail())
                .orElseThrow(() -> new BusinessException(ErrorCode.INVALID_EMAIL));

        if (bCryptPasswordEncoder.matches(request.getPassword(), member.getPassword())) {
            jwtTokenDto = tokenManager.createJwtTokenDto(member.getMemberId(), member.getRole());
            member.updateRefreshToken(jwtTokenDto);
            log.info("userInfo : {}", member);
            return LoginDto.Response.of(jwtTokenDto);
        } else {
            throw new BusinessException(ErrorCode.INVALID_PASSWORD);
        }
    }
}
