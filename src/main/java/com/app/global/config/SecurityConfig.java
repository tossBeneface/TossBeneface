package com.app.global.config;

import com.app.global.filter.LoggingFilter;
import com.app.global.jwt.service.TokenManager;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@Slf4j
public class SecurityConfig {

    @Value("${token.access-token-expiration-time}")
    private String accessTokenExpirationTime;

    @Value("${token.refresh-token-expiration-time}")
    private String refreshTokenExpirationTime;

    @Value("${token.secret}")
    private String tokenSecret;

    @Bean
    public TokenManager tokenManager() {
        return new TokenManager(accessTokenExpirationTime, refreshTokenExpirationTime, tokenSecret);
    }

    @Bean
    public BCryptPasswordEncoder bCryptPasswordEncoder(){
        return new BCryptPasswordEncoder();
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        log.info("SecurityConfig - SecurityFilterChain 설정 시작");
        http
                .csrf(csrf -> csrf.disable()) // CSRF 비활성화
                .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS)) // JWT방식에서는 세션이 필요 없음
                .authorizeHttpRequests(auth -> auth
                        .requestMatchers("/api/health","/api/join", "/api/login", "/api/access-token/issue", "/h2-console/**", "/api/qnaboard/test", "/api/card-benefits").permitAll() // 인증 없이 접근 허용
                        .anyRequest().authenticated() // 그 외 요청은 인증 필요
                )
                .headers(headers -> headers.frameOptions(frameOptions -> frameOptions.disable())); // H2 콘솔 사용을 위한 설정

        http.securityContext(context -> context.requireExplicitSave(false));
        http.addFilterBefore(new LoggingFilter(), UsernamePasswordAuthenticationFilter.class);

        return http.build();
    }

}
