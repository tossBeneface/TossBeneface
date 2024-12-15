package com.app.api.exceptionTest.dto;

import javax.validation.constraints.Max;
import javax.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;

@Getter @Setter
public class BindExceptionTestDto {

    @NotBlank(message = "해당 값은 필수 입력값 입니다.")
    private String value1;

    @Max(value = 10, message = "최대 입력값은 10입니다.")
    private Integer value2;

}
