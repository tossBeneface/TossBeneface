package com.app.global.resolver.memberInfo;

import com.app.domain.member.constant.Role;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class MemberInfoDto {

    private Long memberId;
    private Role role;

}
