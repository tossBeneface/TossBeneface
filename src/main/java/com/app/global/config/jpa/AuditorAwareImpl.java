package com.app.global.config.jpa;

import java.util.Optional;
import javax.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.AuditorAware;
import org.springframework.util.StringUtils;

public class AuditorAwareImpl implements AuditorAware<String> {

    // 현재 요청에 대한 uri 정보를 여기서 가져온다
    @Autowired
    private HttpServletRequest httpServletRequest;

    // 이 메소드의 반환값으로 생성자나 수정자에 등록될 uri정보를 전해줘야 한다.
    @Override
    public Optional<String> getCurrentAuditor() {
        String modifiedBy = httpServletRequest.getRequestURI();

//        HttpServletRequest에서 찾으려고 하는 uri 정보가 빈 값이면 수정자를 Unknown으로 설정
        if (!StringUtils.hasText(modifiedBy)) {
            modifiedBy = "unknown";
        }
//        of(X)  는 X 가 null 이 아님이 확실할 때만 사용해야 하며, X 가 null 이면 NullPointerException이 발생 한다.
        return Optional.of(modifiedBy);
    }
}
