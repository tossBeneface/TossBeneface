package com.app.global.util;

import com.app.global.error.ErrorCode;
import com.app.global.error.exception.AuthenticationException;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.nio.charset.StandardCharsets;
import java.security.Key;

@Component
public class JwtUtils {
    private final Key key;

    public JwtUtils(@Value("${token.secret}") String tokenSecret) {
        this.key = Keys.hmacShaKeyFor(tokenSecret.getBytes(StandardCharsets.UTF_8));
    }

    /**
     * JWT에서 memberId 추출
     */
    public Long extractMemberIdFromToken(String token) {
        try {
            Claims claims = getTokenClaims(token);
            return Long.valueOf(claims.get("memberId").toString());
        } catch (Exception e) {
            throw new AuthenticationException(ErrorCode.NOT_VALID_TOKEN);
        }
    }

    /**
     * JWT 토큰 유효성 검사
     */
    public void validateToken(String token) {
        try {
            Jwts.parserBuilder()
                    .setSigningKey(key)
                    .build()
                    .parseClaimsJws(token);
        } catch (ExpiredJwtException e) {
            throw new AuthenticationException(ErrorCode.TOKEN_EXPIRED);
        } catch (Exception e) {
            throw new AuthenticationException(ErrorCode.NOT_VALID_TOKEN);
        }
    }

    /**
     * 토큰의 Claims 정보 추출
     */
    public Claims getTokenClaims(String token) {
        try {
            return Jwts.parserBuilder()
                    .setSigningKey(key)
                    .build()
                    .parseClaimsJws(token)
                    .getBody();
        } catch (Exception e) {
            throw new AuthenticationException(ErrorCode.NOT_VALID_TOKEN);
        }
    }
}
