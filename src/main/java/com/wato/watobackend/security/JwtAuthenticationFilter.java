package com.wato.watobackend.security;

import com.wato.watobackend.exception.constant.Error;
import com.wato.watobackend.util.RequestUtil;
import com.wato.watobackend.util.ResponseUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;
import org.springframework.security.web.util.matcher.NegatedRequestMatcher;
import org.springframework.security.web.util.matcher.OrRequestMatcher;
import org.springframework.security.web.util.matcher.RequestMatcher;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.stream.Collectors;

import static com.wato.watobackend.config.SecurityConfig.WHITELIST;

@Slf4j
@RequiredArgsConstructor
public class JwtAuthenticationFilter extends OncePerRequestFilter {

    private final JwtProvider jwtProvider;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        try {
            if (isAuthRequired(request)) {
                String token = RequestUtil.getAccessToken(request);
                if (token != null) {
                    Authentication authentication = jwtProvider.getAuthentication(token);
                    SecurityContextHolder.getContext().setAuthentication(authentication);
                }
            }

            filterChain.doFilter(request, response);
        } catch (Exception e) {
            ResponseUtil.responseError(response, Error.AUTH_FAILED);
            log.error("[JwtAuthenticationFilter] message: {}", e.getMessage());
        }
    }

    private boolean isAuthRequired(HttpServletRequest request) {
        RequestMatcher rm = new NegatedRequestMatcher(new OrRequestMatcher(Arrays.stream(WHITELIST).map(AntPathRequestMatcher::new).collect(Collectors.toList())));
        return rm.matcher(request).isMatch();
    }
}
