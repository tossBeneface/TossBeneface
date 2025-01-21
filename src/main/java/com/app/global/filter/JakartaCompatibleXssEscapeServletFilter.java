package com.app.global.filter;

import com.app.global.adapter.FilterConfigAdapter;
import jakarta.servlet.*;
import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;

import java.io.IOException;

@Slf4j
public class JakartaCompatibleXssEscapeServletFilter implements Filter {

    private final javax.servlet.Filter delegate;

    public JakartaCompatibleXssEscapeServletFilter(javax.servlet.Filter delegate) {
        this.delegate = delegate;
    }

    @Override
    public void init(jakarta.servlet.FilterConfig filterConfig) throws ServletException {
        try {
            delegate.init(new FilterConfigAdapter(filterConfig));
        } catch (javax.servlet.ServletException e) {
            throw new jakarta.servlet.ServletException(e);
        }
    }

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
            throws IOException, ServletException {
        if (request instanceof HttpServletRequest httpRequest) {
//            log.info("XSS 필터 호출: {}", httpRequest.getRequestURI());
            // HttpServletRequest로 처리
            chain.doFilter(httpRequest, response);
        } else {
            throw new ServletException("Invalid request type");
        }
    }




    @Override
    public void destroy() {
        delegate.destroy();
    }
}