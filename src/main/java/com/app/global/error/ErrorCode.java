package com.app.global.error;

import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
public enum ErrorCode {

    TEST(HttpStatus.INTERNAL_SERVER_ERROR, "001", "business exception test"),

    // 인증 관련 오류들
    TOKEN_EXPIRED(HttpStatus.UNAUTHORIZED, "A-001", "토큰이 만료되었습니다."),
    NOT_VALID_TOKEN(HttpStatus.UNAUTHORIZED, "A-002", "해당 토큰은 유효한 토큰이 아닙니다."),
    NOT_EXISTS_AUTHORIZATION(HttpStatus.UNAUTHORIZED, "A-003", "Authorization Header가 반값입니다."),
    NOT_VALID_BEARER_GRANT_TYPE(HttpStatus.UNAUTHORIZED, "A-004", "인증 타입이 Bearer 타입이 아닙니다."),
    REFRESH_TOKEN_NOT_FOUND(HttpStatus.UNAUTHORIZED, "A-005", "해당 REFRESH TOKEN은 존재하지 않습니다."),
    REFRESH_TOKEN_EXPIRED(HttpStatus.UNAUTHORIZED, "A-006", "해당 REFRESH TOKEN은 만료됐습니다."),
    NOT_ACCESS_TOKEN_TYPE(HttpStatus.UNAUTHORIZED, "A-007", "해당 토큰은 ACCESS TOKEN이 아닙니다."),
    // 인가(권한) 관련 오류
    FORBIDDEN_ADMIN(HttpStatus.FORBIDDEN, "A-008", "관리자 Role이 아닙니다. 관리자만 접근 가능합니다."),

    // 회원
    ALREADY_REGISTERED_MEMBER(HttpStatus.BAD_REQUEST, "M-001", "이미 가입된 회원입니다."),
    MEMBER_NOT_EXIST(HttpStatus.BAD_REQUEST, "M-002", "해당 회원은 존재하지 않습니다."),
    INVALID_EMAIL(HttpStatus.BAD_REQUEST, "M-003", "이메일 주소가 잘못되었습니다."),
    INVALID_PASSWORD(HttpStatus.BAD_REQUEST, "M-004", "비밀번호가 틀렸습니다."),
    ;


    ErrorCode(HttpStatus httpStatus, String errorCode, String message) {
        this.httpStatus = httpStatus;
        this.errorCode = errorCode;
        this.message = message;
    }

    private HttpStatus httpStatus;
    private String errorCode;
    private String message;
}
