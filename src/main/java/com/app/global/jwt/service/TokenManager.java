package com.app.global.jwt.service;

import com.app.domain.member.constant.Role;
import com.app.global.jwt.constant.GrantType;
import com.app.global.jwt.constant.TokenType;
import com.app.global.jwt.dto.JwtTokenDto;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletResponse;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.nio.charset.StandardCharsets;
import java.security.Key;
import java.util.Date;

@Slf4j
@Component
public class TokenManager {

    // application.yml에서 가져올 것임
    @Getter
    private final String accessTokenExpirationTime;
    @Getter
    private final String refreshTokenExpirationTime;
    private final Key key;

    public TokenManager(
            @Value("${token.access-token-expiration-time}") String accessTokenExpirationTime,
            @Value("${token.refresh-token-expiration-time}") String refreshTokenExpirationTime,
            @Value("${token.secret}") String tokenSecret) {
        this.accessTokenExpirationTime = accessTokenExpirationTime;
        this.refreshTokenExpirationTime = refreshTokenExpirationTime;
        this.key = Keys.hmacShaKeyFor(tokenSecret.getBytes(StandardCharsets.UTF_8));
    }
    // 액세스 토큰에 멤버 아이디와 역할을 담아서 반환할 것이다.
    public JwtTokenDto createJwtTokenDto(Long memberId, Role role, HttpServletResponse response) {
        Date accessTokenExpireTime = createAccessTokenExpireTime();
        Date refreshTokenExpireTime = createRefreshTokenExpireTime();

        String accessToken = createAccessToken(memberId, role, accessTokenExpireTime);
        String refreshToken = createRefreshToken(memberId, refreshTokenExpireTime);

        // RefreshToken을 HttpOnly 쿠키로 설정
        addHttpOnlyCookie(response, "refreshToken", refreshToken);

        return JwtTokenDto.builder()
            .memberId(String.valueOf(memberId))
            .grantType(GrantType.BEARER.getType())
            .accessToken(accessToken)
            .refreshToken(refreshToken)
            .accessTokenExpireTime(accessTokenExpireTime)
            .refreshTokenExpireTime(refreshTokenExpireTime)
            .build();
    }

    // HttpOnly 쿠키 생성
    private void addHttpOnlyCookie(HttpServletResponse response, String name, String value) {
        Cookie cookie = new Cookie(name, value);
        cookie.setHttpOnly(true); // JavaScript 접근 방지
        cookie.setSecure(true);  // HTTPS 전용
        cookie.setPath("/");     // 전체 도메인에 적용
        cookie.setMaxAge((int) Long.parseLong(refreshTokenExpirationTime) / 1000); // ms -> s 변환
        response.addCookie(cookie);
    }


    // 액세스 토큰의 만료 시간을 반환해 주는 메서드
    public Date createAccessTokenExpireTime() {
        // 현재 시간으로 부터 15분 뒤의 시간을 반환
        return new Date(System.currentTimeMillis() + Long.parseLong(accessTokenExpirationTime));
    }

    public Date createRefreshTokenExpireTime() {
        return new Date(System.currentTimeMillis() + Long.parseLong(refreshTokenExpirationTime));
    }

    public String createAccessToken(Long memberId, Role role, Date expirationTime) {
        String accessToken = Jwts.builder()
            .setSubject(TokenType.ACCESS.name())
            .setIssuedAt(new Date()) // 현재시간으로 토큰발급시간 설정
            .setExpiration(expirationTime)
            .claim("memberId", memberId) // 회원 아이디
            .claim("role", role)
            .signWith(key, SignatureAlgorithm.HS512) // 수정된 부분
            .setHeaderParam("type", "JWT")
            .compact();
        log.debug("Token Expiration: {}", expirationTime);
        return accessToken;
    }

    public String createRefreshToken(Long memberId, Date expirationTime) {
        String refreshToken = Jwts.builder()
            .setSubject(TokenType.REFRESH.name())
            .setIssuedAt(new Date()) // 현재시간으로 토큰발급시간 설정
            .setExpiration(expirationTime)
            .claim("memberId", memberId) // 회원 아이디, refresh token에는 많은 정보를 담지 않기 위해 role은 제외
            .signWith(key, SignatureAlgorithm.HS512) // 수정된 부분
            .setHeaderParam("type", "JWT")
            .compact();
        return refreshToken;
    }

    // JwtUtils로 이동

    // 클라이언트에서 토큰들이 어써라이제이션 헤더에 담겨서 들어올 텐데 이 값들이 발급한 만료되지 않은 토큰인지 검증
    // -> access 토큰 refresh 토큰 둘 다 검증 가능
//    public void validateToken(String token) {
//        try {
//            Jwts.parserBuilder()
//                    .setSigningKey(key)
//                    .build()
//                    .parseClaimsJws(token);
//        } catch (ExpiredJwtException e) {
//            log.info("token 만료", e);
//            throw new AuthenticationException(ErrorCode.TOKEN_EXPIRED);
//        } catch (Exception e) { // 위조/변조/발급하지 않은 토큰일때
//            log.info("유효하지 않은 token", e);
//            throw new AuthenticationException(ErrorCode.NOT_VALID_TOKEN);
//        }
//    }

    // 토큰 정보를 서버에서 사용하기 위해 페이로드에 있는 클레임 정보들을 가지고 오는 메서드
//    public Claims getTokenClaims(String token) {
//        Claims claims;
//        try {
//            claims = Jwts.parserBuilder()
//                    .setSigningKey(key)
//                    .build()
//                    .parseClaimsJws(token)
//                    .getBody();
//        } catch (Exception e) {
//            log.info("유효하지 않은 token", e);
//            throw new AuthenticationException(ErrorCode.NOT_VALID_TOKEN);
//        }
//        return claims;
//    }

}
