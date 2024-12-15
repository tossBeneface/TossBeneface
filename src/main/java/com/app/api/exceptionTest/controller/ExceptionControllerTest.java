package com.app.api.exceptionTest.controller;

import com.app.api.exceptionTest.dto.BindExceptionTestDto;
import com.app.api.exceptionTest.dto.TestEnum;
import com.app.global.error.ErrorCode;
import com.app.global.error.exception.BusinessException;
import javax.validation.Valid;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/exception")
public class ExceptionControllerTest {

    @GetMapping("/bind-exception-test")
    public String bindExceptionTest(@Valid BindExceptionTestDto bindExceptionTestDto) {
        return "ok";
    }

    @GetMapping("/type-exception-test")
    public String typeMismatchExceptionTest(TestEnum testEnum) {
        return "ok";
    }

    @GetMapping("/business-exception-test")
    public String businessExceptionTest(String isError) {

        if("true".equals(isError)) {
            throw new BusinessException(ErrorCode.TEST);
        }
        return "ok";
    }

    // 나머지 모든 일반적 에러
    @GetMapping("/exception-test")
    public String exceptionTest(String isError) {

        if("true".equals(isError)) {
            throw new IllegalArgumentException("일반적인 모든 예외를 받는다.");
        }
        return "ok";
    }
}
