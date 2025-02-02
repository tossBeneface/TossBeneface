package com.app.domain.member.constant;

public enum Role {

    USER, OWNER, ADMIN;

    public static Role from(String role) {
        try {
            return Role.valueOf(role);
        } catch (IllegalArgumentException e) {
            // Handle invalid role input, e.g., log it or return a default role
            return Role.USER; // default role or null if that's preferred
        }
    }

}
