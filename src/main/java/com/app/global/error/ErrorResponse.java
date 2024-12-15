package com.app.global.error;

import java.util.List;
import lombok.Builder;
import lombok.Getter;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;

@Getter
@Builder
public class ErrorResponse {

    private String errorCode;
    private String errorMessage;

    public static ErrorResponse of(String errorCode, String errorMessage) {
        return ErrorResponse.builder()
            .errorCode(errorCode)
            .errorMessage(errorMessage)
            .build();
    }

    public static ErrorResponse of(String errorCode, BindingResult bindingResult) {
        return ErrorResponse.builder()
            .errorCode(errorCode)
            .errorMessage(createErrorMessage(bindingResult))
            .build();

    }

    // 어떤 필드에 어떤 오류가 있는지 알려주는 메서드
    private static String createErrorMessage(BindingResult bindingResult) {
        StringBuilder sb = new StringBuilder();
        boolean isFirst = true;

        // BindingResult에 어떤 에러 메시지가 있는지에 대한 정보를 가져 온다
        List<FieldError> fieldErrors = bindingResult.getFieldErrors();
        for (FieldError fieldError : fieldErrors) {
//            if (!isFirst) {
//                sb.append(", ");
//            } else {
//                isFirst = false;
//            }
            // todo 이렇게 해도 되고 이게 보기 편할거 같은데 잘 되나 테스트
            if (isFirst) {
                isFirst = false;
            } else {
                sb.append(", ");
            }
            sb.append("[");
            // 필드명 가져오기
            sb.append(fieldError.getField());
            sb.append("]");
            // 에러 메시지 가져오기
            sb.append(fieldError.getDefaultMessage());
        }
        return sb.toString();
    }
}
