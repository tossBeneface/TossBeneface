package com.app.api.login.validator;

import com.app.domain.member.entity.Member;
import com.app.domain.member.repository.MemberRepository;
import com.app.domain.member.service.MemberService;
import com.app.global.error.ErrorCode;
import com.app.global.error.exception.BusinessException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class LoginValidator {

    private final MemberService memberService;

    public void validateMemberEmail(String email) {
        Optional<Member> optionalMember = memberService.findMemberByEmail(email);
        if (!optionalMember.isEmpty()) {
            throw new BusinessException(ErrorCode.ALREADY_REGISTERED_MEMBER);
        }
    }
}
