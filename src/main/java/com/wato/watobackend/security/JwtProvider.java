package com.wato.watobackend.security;

import com.wato.watobackend.dto.EmailAuthDto;
import com.wato.watobackend.dto.TokenDto;
import com.wato.watobackend.exception.ApiException;
import com.wato.watobackend.exception.constant.Error;
import io.jsonwebtoken.Claims;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import java.util.Arrays;
import java.util.Collection;
import java.util.stream.Collectors;

@Slf4j
@RequiredArgsConstructor
@Component
public class JwtProvider {

    private final JwtUtil jwtToken;

    public TokenDto generateToken(Long id, String email) {
        return jwtToken.generateToken(id, email);
    }

    public TokenDto generateTokenByCode(Long id, String email, Integer code) {
        return jwtToken.generateTokenByCode(id, email, code);
    }

    public Authentication getAuthentication(String token) {
        Claims claims = getClaims(token);

        String id = claims.get("id").toString();
        if (StringUtils.isEmpty(id)) {
            throw new ApiException(Error.AUTH_FAILED);
        }

        return userPrincipal(id);
    }

    public EmailAuthDto getEmailAuthentication(String token) {
        Claims claims = getClaims(token);

        String email = claims.getSubject();
        Integer code = (Integer) claims.get("code");
        if (StringUtils.isEmpty(email) || code == null) {
            throw new ApiException(Error.AUTH_FAILED);
        }

       return EmailAuthDto.builder().email(email).code(code).build();
    }

    private static UsernamePasswordAuthenticationToken userPrincipal(String id) {
        Collection<? extends GrantedAuthority> authorities =
                Arrays.stream(new String[]{"role"})
                        .map(SimpleGrantedAuthority::new)
                        .collect(Collectors.toList());

        User principal = new User(id, "", authorities);

        return new UsernamePasswordAuthenticationToken(principal, "", principal.getAuthorities());
    }

    private Claims getClaims(String token) {
        Claims claims = jwtToken.validateToken(token);
        if (claims == null) throw new ApiException(Error.TOKEN_VALID_FAILED);
        return claims;
    }
}
