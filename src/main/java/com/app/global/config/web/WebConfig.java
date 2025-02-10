package com.app.global.config.web;

import com.app.global.interceptor.AdminAuthorizationInterceptor;
import com.app.global.interceptor.AuthenticationInterceptor;
import com.app.global.resolver.memberInfo.MemberInfoArgumentResolver;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import java.util.List;

@Configuration
@Slf4j
@RequiredArgsConstructor
public class WebConfig implements WebMvcConfigurer {

    private final AuthenticationInterceptor authenticationInterceptor;
    private final AdminAuthorizationInterceptor adminAuthorizationInterceptor;
    private final MemberInfoArgumentResolver memberInfoArgumentResolver;
    private final ObjectMapper objectMapper;

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        log.info("WebConfig - Interceptor 등록됨");
        registry.addInterceptor(authenticationInterceptor)
            .order(1) // 인증 인터셉터가 가장 먼저 동작하도록 설정
            .addPathPatterns("/api/**")
            .excludePathPatterns(
                    "/swagger-ui/**", "/v3/api-docs/**", "/h2-console/**",
                    "/api/join", "/api/join/**", "/api/login", "/api/login/**",
                    "/api/access-token/issue", "/api/access-token/issue/**", "/api/health", "/api/qnaboard/test",
                    "/api/card-benefits", "/api/card-benefits/**", "/api/user-data-test", "/api/user-data-test/**",
                    "/api/v1/payments/**", "/api/faces/**", "/api/products/**", "/api/flow", "/api/member/name/**",
                    "/api/qr/authenticate");
        registry.addInterceptor(adminAuthorizationInterceptor)
            .order(2)
            .addPathPatterns("/api/admin/**");
        log.debug("등록된 인터셉터 경로: /api/**");
//        log.debug("Interceptor 호출: {}", request.getRequestURI());

    }

    @Override
    public void addCorsMappings(CorsRegistry registry) {
//        registry.addMapping("/api/**")
        registry.addMapping("/**")
//                .allowedOrigins("http://localhost:8082")
                .allowedOriginPatterns("http://localhost:3000", "https://app.tossbeneface.com", "https://www.tossbeneface.com") // 허용할 도메인 명시
                .allowedMethods(
                HttpMethod.HEAD.name(),
                HttpMethod.GET.name(),
                HttpMethod.POST.name(),
                HttpMethod.PUT.name(),
                HttpMethod.PATCH.name(),
                HttpMethod.DELETE.name(),
                HttpMethod.OPTIONS.name()
            ).allowedHeaders("Content-Type", "Authorization", "Accept")
           .allowCredentials(true); // 쿠키 허용;
    }

    @Override
    public void addArgumentResolvers(List<HandlerMethodArgumentResolver> resolvers) {
        resolvers.add(memberInfoArgumentResolver);
    }

    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
//        // '/widget/**' URL을 'classpath:/templates/widget/' 디렉토리로 매핑
//        registry.addResourceHandler("/widget/**")
//                .addResourceLocations("classpath:/templates/widget/");
//
//        // '/payment/**' URL을 'classpath:/templates/payment/' 디렉토리로 매핑
//        registry.addResourceHandler("/payment/**")
//                .addResourceLocations("classpath:/templates/payment/");
    }

    @Bean
    public RestTemplate restTemplate() {
        return new RestTemplate();
    }

}
