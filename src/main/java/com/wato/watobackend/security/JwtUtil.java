package com.wato.watobackend.security;

import com.wato.watobackend.dto.TokenDto;
import io.jsonwebtoken.*;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.security.Key;
import java.util.Date;
import java.util.function.Function;

import static org.springframework.security.oauth2.core.endpoint.OAuth2ParameterNames.ACCESS_TOKEN;

@Slf4j
@Component
public class JwtUtil implements InitializingBean {

    @Value("${jwt.accessToken.duration}")
    public Long jwtAccessTokenDuration;

    @Value("${jwt.email.auth.duration}")
    public Long jwtEmailAuthTokenDuration;

    @Value("${jwt.secret}")
    private String secret;

    private Key secretKey;

    @Override
    public void afterPropertiesSet() throws Exception {
        byte[] keyBytes = Decoders.BASE64.decode(secret);
        secretKey = Keys.hmacShaKeyFor(keyBytes);
    }

    public TokenDto generateToken(Long id, String email) {
        Date now = new Date(System.currentTimeMillis());
        Date expirationDate = new Date(System.currentTimeMillis() + jwtAccessTokenDuration);

        String token = Jwts.builder()
                .signWith(secretKey, SignatureAlgorithm.HS256)
                .setSubject(email)
                .claim("id", id)
                .setIssuer("Wato")
                .setIssuedAt(now)
                .setExpiration(expirationDate)
                .compact();

        return TokenDto.builder()
                .token(token)
                .expiration(expirationDate)
                .build();
    }

    public TokenDto generateTokenByCode(Long id, String email, Integer code) {
        Date now = new Date();
        Date expirationDate = new Date(System.currentTimeMillis() +  jwtEmailAuthTokenDuration); // 이메일 인증코드 유효기간 10분

        String token = Jwts.builder()
                .signWith(secretKey, SignatureAlgorithm.HS256)
                .setSubject(email)
                .claim("id", id.toString())
                .claim("code", code)
                .setIssuer("Wato")
                .setIssuedAt(now)
                .setExpiration(expirationDate)
                .compact();

        return TokenDto.builder()
                .token(token)
                .expiration(expirationDate)
                .build();
    }

    public Claims validateToken(String token) {
        try {
            return Jwts.parserBuilder().setSigningKey(secretKey).build().parseClaimsJws(token).getBody();
        } catch (io.jsonwebtoken.security.SecurityException | MalformedJwtException e) {
            log.error("잘못된 JWT 서명입니다.");
        } catch (ExpiredJwtException e) {
            log.error("만료된 JWT 토큰입니다.");
        } catch (UnsupportedJwtException e) {
            log.error("지원하지 않는 JWT 토큰입니다.");
        } catch (IllegalArgumentException e) {
            log.error("JWT 토큰이 잘못되었습니다.");
        }
        return null;
    }

    private <T> T getClaimFromToken(String token, Function<Claims, T> claimsResolver) {
        Claims claims = getAllClaimsFromToken(token);
        return claimsResolver.apply(claims);
    }

    private Claims getAllClaimsFromToken(String token) {
        return Jwts.parserBuilder().setSigningKey(secretKey).build().parseClaimsJws(token).getBody();
    }
}
