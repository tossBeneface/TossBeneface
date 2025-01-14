package com.app.global.config.jpa;

import jakarta.servlet.http.HttpServletRequest;
import org.springframework.data.domain.AuditorAware;
import org.springframework.util.StringUtils;
import org.springframework.web.context.request.RequestAttributes;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import java.util.Optional;

public class AuditorAwareImpl implements AuditorAware<String> {

    // 이 메소드의 반환값으로 생성자나 수정자에 등록될 uri정보를 전해줘야 한다.
    @Override
    public Optional<String> getCurrentAuditor() {
        // RequestContextHolder를 통해 HttpServletRequest가 있는지 확인
        RequestAttributes attributes = RequestContextHolder.getRequestAttributes();
        if (attributes instanceof ServletRequestAttributes) {
            HttpServletRequest request = ((ServletRequestAttributes) attributes).getRequest();
            String modifiedBy = request.getRequestURI();

            // HttpServletRequest에서 찾으려고 하는 uri 정보가 빈 값이면 수정자를 Unknown으로 설정
            if (!StringUtils.hasText(modifiedBy)) {
                modifiedBy = "unknown";
            }
            return Optional.of(modifiedBy);
        }
        // RequestAttributes가 없을 경우 "unknown" 반환
        return Optional.of("unknown");
    }
}
