package com.app.global.error.exception;

import com.app.global.error.ErrorCode;
import lombok.Getter;

@Getter
public class BusinessException extends RuntimeException{

    private ErrorCode errorCode;

    public BusinessException(ErrorCode errorCode) {
        super(errorCode.getMessage()); // 부모 생성자에 비즈니스익셉션이 발생할 때 받은 에러코드의 메시지를 넣어준다
        this.errorCode = errorCode;
    }
}
