package com.app.domain.member.constant;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public enum MemberType {

    KAKAO;

    public static MemberType from(String type) {
        // valueOf() 메소드는 전달된 문자열과 일치하는 해당 열거체의 상수를 반환
        return MemberType.valueOf(type.toUpperCase());
    }

    public static boolean isMemberType(String type) {
        List<MemberType> memberTypes = Arrays.stream(MemberType.values())
            .filter(memberType -> memberType.name().equals(type))
            .collect(Collectors.toList());
        return memberTypes.size() != 0;
    }
}
