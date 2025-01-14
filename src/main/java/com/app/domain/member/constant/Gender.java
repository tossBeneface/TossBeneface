package com.app.domain.member.constant;

public enum Gender {

    MALE, FEMALE;

    public static Gender from(String gender) {
        return Gender.valueOf(gender);
    }
}
