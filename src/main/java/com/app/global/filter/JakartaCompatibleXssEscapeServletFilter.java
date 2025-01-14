package com.app.global.filter;

import jakarta.servlet.Filter;
import jakarta.servlet.FilterChain;
import jakarta.servlet.FilterConfig;
import jakarta.servlet.ServletException;
import jakarta.servlet.ServletRequest;
import jakarta.servlet.ServletResponse;
import java.io.IOException;

public class JakartaCompatibleXssEscapeServletFilter implements Filter {

    private final javax.servlet.Filter delegate; // javax.servlet.Filter를 명시적으로 사용

    public JakartaCompatibleXssEscapeServletFilter(javax.servlet.Filter delegate) {
        this.delegate = delegate;
    }

    @Override
    public void init(FilterConfig filterConfig) throws ServletException {
        try {
            delegate.init(null); // 위임 필터 초기화
        } catch (javax.servlet.ServletException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
            throws IOException, jakarta.servlet.ServletException {
        try {
            delegate.doFilter(
                    (javax.servlet.ServletRequest) request,
                    (javax.servlet.ServletResponse) response,
                    (javax.servlet.FilterChain) chain
            );
        } catch (javax.servlet.ServletException e) {
            throw new jakarta.servlet.ServletException(e); // 예외 변환
        }
    }




    @Override
    public void destroy() {
        delegate.destroy();
    }
}
