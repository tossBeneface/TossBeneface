package com.app.domain.member.constant;

public enum MemberStatus {

    ACTIVATE, DEACTIVATE;

    public static MemberStatus from(String memberStatus) {
        return MemberStatus.valueOf(memberStatus);
    }
}
