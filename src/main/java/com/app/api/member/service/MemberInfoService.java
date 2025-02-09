package com.app.api.member.service;

import com.app.api.member.dto.MemberInfoResponseDto;
import com.app.domain.member.entity.Member;
import com.app.domain.member.service.MemberService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class MemberInfoService {

    private final MemberService memberService;

    @Transactional(readOnly = true)
    public MemberInfoResponseDto getMemberInfo(Long memberId) {
        Member member = memberService.findMemberById(memberId);
        return MemberInfoResponseDto.of(member);
    }

    public String getMemberName(Long memberId) {
        Member member = memberService.findMemberById(memberId);
        return member.getMemberName();  // 회원 엔티티에 member_name 필드가 있을 경우 사용 (메서드 이름은 실제 엔티티에 맞게 조정)
    }
}
