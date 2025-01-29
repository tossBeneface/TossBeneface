package com.app.api.login.service;

import com.app.api.login.dto.JoinDto;
import com.app.api.login.dto.LoginDto;
import com.app.api.login.validator.LoginValidator;
import com.app.domain.member.constant.Gender;
import com.app.domain.member.constant.MemberStatus;
import com.app.domain.member.constant.Role;
import com.app.domain.member.entity.Member;
import com.app.domain.member.repository.MemberRepository;
import com.app.domain.member.service.MemberService;
import com.app.global.error.ErrorCode;
import com.app.global.error.exception.BusinessException;
import com.app.global.jwt.dto.JwtTokenDto;
import com.app.global.jwt.service.TokenManager;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

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

    public JoinDto.Response join(JoinDto.Request request, HttpServletResponse httpServletResponse) {

        JwtTokenDto jwtTokenDto;

        loginValidator.validateMemberEmail(request.getEmail());

        Member member = Member.builder()
                .email(request.getEmail())
                .password(bCryptPasswordEncoder.encode(request.getPassword()))
                .memberName(request.getMemberName())
                .phoneNumber(request.getPhoneNumber())
                .gender(Gender.valueOf(request.getGender().toUpperCase()))
                .role(Role.valueOf(request.getRole().toUpperCase()))
                .memberStatus(MemberStatus.ACTIVATE)
                //TODO 기본이미지 처리 -> 프론트에 넘겨서?
                .profileImg(Optional.ofNullable(request.getProfileImg()).orElse(""))
                .budget(Optional.ofNullable(request.getProfileImg()).orElse(""))
                .build();

        member = memberService.registerMember(member);
        jwtTokenDto = tokenManager.createJwtTokenDto(member.getMemberId(), member.getRole(), httpServletResponse);
        log.info("memberInfo : {}", member);
        member.updateRefreshToken(jwtTokenDto);

        return JoinDto.Response.of(jwtTokenDto, request);

    }

    public LoginDto.Response login(LoginDto.Request request, HttpServletResponse httpServletResponse) {
        JwtTokenDto jwtTokenDto;

        Member member = memberRepository.findByEmail(request.getEmail())
                .orElseThrow(() -> new BusinessException(ErrorCode.INVALID_EMAIL));

        if (bCryptPasswordEncoder.matches(request.getPassword(), member.getPassword())) {
            jwtTokenDto = tokenManager.createJwtTokenDto(member.getMemberId(), member.getRole(), httpServletResponse);
            member.updateRefreshToken(jwtTokenDto);
            LoginDto.Response response = LoginDto.Response.of(jwtTokenDto);
            response.setEmail(member.getEmail());
            response.setMemberName(member.getMemberName());
            response.setPhoneNumber(member.getPhoneNumber());
            if (!member.getBudget().isEmpty()) {
                response.setBudget(member.getBudget());
            }
            if (!member.getProfileImg().isEmpty()) {
                response.setProfileImg(member.getProfileImg());
            }
            response.setGender(member.getGender().toString());
            response.setRole(member.getRole().toString());
            log.info("userInfo : {}", member);

            return response;
        } else {
            throw new BusinessException(ErrorCode.INVALID_PASSWORD);
        }
    }
}
