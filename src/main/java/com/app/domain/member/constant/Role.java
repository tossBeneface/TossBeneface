package com.app.domain.member.constant;

public enum Role {

    USER, OWNER, ADMIN;

    public static Role from(String role) {
        return Role.valueOf(role);
    }
}
