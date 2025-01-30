package com.app.global.config;

import com.app.global.filter.LoggingFilter;
import com.app.global.interceptor.AdminAuthorizationInterceptor;
import com.app.global.interceptor.AuthenticationInterceptor;
import com.app.global.jwt.service.TokenManager;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.access.AccessDeniedHandler;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@Slf4j
@RequiredArgsConstructor
public class SecurityConfig {

    private final TokenManager tokenManager;

    @Value("${token.access-token-expiration-time}")
    private String accessTokenExpirationTime;

    @Value("${token.refresh-token-expiration-time}")
    private String refreshTokenExpirationTime;

    @Value("${token.secret}")
    private String tokenSecret;

    @Bean
    public BCryptPasswordEncoder bCryptPasswordEncoder(){
        return new BCryptPasswordEncoder();
    }

    @Bean
    public AuthenticationInterceptor authenticationInterceptor(TokenManager tokenManager) {
        log.info("AuthenticationInterceptor 빈 생성");
        return new AuthenticationInterceptor(tokenManager);
    }

    @Bean
    public AdminAuthorizationInterceptor adminAuthorizationInterceptor() {
        return new AdminAuthorizationInterceptor(tokenManager);
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        log.info("SecurityConfig - SecurityFilterChain 설정 시작");
        http
                .csrf(csrf -> csrf.disable()) // CSRF 비활성화
                .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS)) // JWT방식에서는 세션이 필요 없음
                .authorizeHttpRequests(auth -> auth
                        .requestMatchers("/api/health","/api/join", "/api/login", "/api/access-token/issue", "/h2-console/**", "/api/qnaboard/test", "/api/card-benefits/**", "/api/user-data-test",
                                "/api/user-data-test/**", "/api/faces/**").permitAll() // 인증 없이 접근 허용
                        .anyRequest().authenticated() // 그 외 요청은 인증 필요
                )
                .headers(headers -> headers.frameOptions(frameOptions -> frameOptions.disable())) // H2 콘솔
                .securityContext(context -> context.requireExplicitSave(false))
                .addFilterBefore(new LoggingFilter(), UsernamePasswordAuthenticationFilter.class)
                .exceptionHandling(exceptions -> exceptions
                        // 인증 실패 처리
                        .authenticationEntryPoint(authenticationEntryPoint())
                        // 인가 실패 처리
                        .accessDeniedHandler(accessDeniedHandler())
                );
        return http.build();
    }

    @Bean
    public AuthenticationEntryPoint authenticationEntryPoint() {
        log.info("AuthenticationEntryPoint 빈 생성");
        return (request, response, authException) -> {
            log.warn("401 Unauthorized Error: {}", authException.getMessage());
            response.sendError(HttpServletResponse.SC_UNAUTHORIZED, "Unauthorized");
        };
    }

    @Bean
    public AccessDeniedHandler accessDeniedHandler() {
        log.info("AccessDeniedHandler 빈 생성");
        return (request, response, accessDeniedException) -> {
            log.warn("403 Forbidden Error: {}", accessDeniedException.getMessage());
            response.sendError(HttpServletResponse.SC_FORBIDDEN, "Forbidden");
        };
    }

}
