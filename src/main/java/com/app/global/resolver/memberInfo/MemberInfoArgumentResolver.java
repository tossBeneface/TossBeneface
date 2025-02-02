package com.app.global.resolver.memberInfo;

import com.app.domain.member.constant.Role;
import com.app.global.jwt.service.TokenManager;
import com.app.global.util.JwtUtils;
import io.jsonwebtoken.Claims;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.core.MethodParameter;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.support.WebDataBinderFactory;
import org.springframework.web.context.request.NativeWebRequest;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.method.support.ModelAndViewContainer;

@Component
@RequiredArgsConstructor
public class MemberInfoArgumentResolver implements HandlerMethodArgumentResolver {

    private final TokenManager tokenManager;
    private final JwtUtils jwtUtils;

    @Override
    public boolean supportsParameter(MethodParameter parameter) {
        boolean hasMemberInfoAnnotation = parameter.hasParameterAnnotation(MemberInfo.class);
        boolean hasMemberInfoDto = MemberInfoDto.class.isAssignableFrom(parameter.getParameterType());
        return hasMemberInfoAnnotation && hasMemberInfoDto;
    }

    @Override
    public Object resolveArgument(MethodParameter parameter, ModelAndViewContainer mavContainer,
                                  NativeWebRequest webRequest, WebDataBinderFactory binderFactory) throws Exception {
        HttpServletRequest request = (HttpServletRequest) webRequest.getNativeRequest();
        String authorizationHeader = request.getHeader("Authorization");

        if (authorizationHeader == null || !authorizationHeader.startsWith("Bearer ")) {
            throw new IllegalArgumentException("Authorization header is missing or not in the correct format. Expected format: Bearer <token>");
        }

        String token = authorizationHeader.split(" ")[1];

        Claims tokenClaims = jwtUtils.getTokenClaims(token);
        Long memberId = Long.valueOf((Integer) tokenClaims.get("memberId"));
        String role = (String) tokenClaims.get("role");

        if (memberId == null || role == null) {
            throw new IllegalArgumentException("Missing token claims");
        }

        // role 값이 null이면 예외 처리
        Role userRole = null;
        try {
            userRole = Role.from(role);
        } catch (IllegalArgumentException e) {
            throw new IllegalArgumentException("Invalid role value: " + role);
        }

        return MemberInfoDto.builder()
                .memberId(memberId)
                .role(userRole)
                .build();
    }
}
