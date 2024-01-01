package com.wato.watobackend.security.oauth2;

import com.wato.watobackend.dto.TokenDto;
import com.wato.watobackend.model.RefreshToken;
import com.wato.watobackend.model.constant.AuthType;
import com.wato.watobackend.model.constant.Role;
import com.wato.watobackend.repository.RefreshTokenRepository;
import com.wato.watobackend.security.JwtProvider;
import com.wato.watobackend.security.UserPrincipal;
import com.wato.watobackend.util.CookieUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.oauth2.client.authentication.OAuth2AuthenticationToken;
import org.springframework.security.oauth2.core.oidc.user.OidcUser;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationSuccessHandler;
import org.springframework.stereotype.Component;
import org.springframework.web.util.UriComponentsBuilder;

import javax.servlet.ServletException;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Collection;
import java.util.Optional;

import static com.wato.watobackend.security.oauth2.OAuth2AuthenticationFailureHandler.REDIRECT_URI_PARAM_COOKIE_NAME;
import static org.springframework.security.oauth2.core.endpoint.OAuth2ParameterNames.ACCESS_TOKEN;
import static org.springframework.security.oauth2.core.endpoint.OAuth2ParameterNames.REFRESH_TOKEN;

@Slf4j
@RequiredArgsConstructor
@Component
public class OAuth2AuthenticationSuccessHandler extends SimpleUrlAuthenticationSuccessHandler {

    @Value("${jwt.accessToken.duration}")
    public Long jwtAccessTokenDuration;

    @Value("${jwt.refreshToken.duration}")
    public Long jwtRefreshTokenDuration;

    private final JwtProvider jwtProvider;
    private final RefreshTokenRepository refreshTokenRepository;
    private final OAuth2AuthorizationRequestBasedOnCookieRepository authorizationRequestRepository;

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws IOException, ServletException {
        String targetUrl = determineTargetUrl(request, response, authentication);

        if (response.isCommitted()) {
            logger.debug("Response has already been committed. Unable to redirect to " + targetUrl);
            return;
        }

        clearAuthenticationAttributes(request, response);
        getRedirectStrategy().sendRedirect(request, response, targetUrl);
    }

    protected String determineTargetUrl(HttpServletRequest request, HttpServletResponse response, Authentication authentication) {
        Optional<String> redirectUri = CookieUtil.getCookie(request, REDIRECT_URI_PARAM_COOKIE_NAME)
                .map(Cookie::getValue);

        String targetUrl = redirectUri.orElse(getDefaultTargetUrl());
        UserPrincipal user = (UserPrincipal) authentication.getPrincipal();

        TokenDto accessTokenDto = jwtProvider.generateToken(user.getId(), ACCESS_TOKEN);
        TokenDto refreshTokenDto = jwtProvider.generateToken(user.getId(),  REFRESH_TOKEN);
        log.info("@@@@@@@@ AccessTokne : {}", accessTokenDto);
        log.info("@@@@@@@@ refreshTokenDto : {}", refreshTokenDto);

        RefreshToken refreshToken = refreshTokenRepository.findByUserId(Long.parseLong(user.getId()));
        if (refreshToken == null) {
            refreshToken = RefreshToken.builder()
                    .userId(Long.parseLong(user.getId()))
                    .build();
        }

        refreshToken.setRefreshToken(refreshTokenDto.getToken());
        refreshToken.setExpiration(refreshTokenDto.getExpiration());
        refreshTokenRepository.save(refreshToken);

        CookieUtil.deleteCookie(request, response, ACCESS_TOKEN);
        CookieUtil.addCookie(response, ACCESS_TOKEN, accessTokenDto.getToken(), jwtAccessTokenDuration.intValue() / 1000);

        CookieUtil.deleteCookie(request, response, REFRESH_TOKEN);
        CookieUtil.addCookie(response, REFRESH_TOKEN, refreshTokenDto.getToken(), jwtRefreshTokenDuration.intValue() / 1000);

        return UriComponentsBuilder.fromUriString(targetUrl).build().toUriString();
    }

    protected void clearAuthenticationAttributes(HttpServletRequest request, HttpServletResponse response) {
        super.clearAuthenticationAttributes(request);
        authorizationRequestRepository.removeAuthorizationRequestCookies(request, response);
    }

    private boolean hasAuthority(Collection<? extends GrantedAuthority> authorities, String authority) {
        if (authorities == null) {
            return false;
        }

        for (GrantedAuthority grantedAuthority : authorities) {
            if (authority.equals(grantedAuthority.getAuthority())) {
                return true;
            }
        }
        return false;
    }

//    private boolean isAuthorizedRedirectUri(String uri) {
//        URI clientRedirectUri = URI.create(uri);
//
//        return appProperties.getOauth2().getAuthorizedRedirectUris()
//                .stream()
//                .anyMatch(authorizedRedirectUri -> {
//                    // Only validate host and port. Let the clients use different paths if they want to
//                    URI authorizedURI = URI.create(authorizedRedirectUri);
//                    if(authorizedURI.getHost().equalsIgnoreCase(clientRedirectUri.getHost())
//                            && authorizedURI.getPort() == clientRedirectUri.getPort()) {
//                        return true;
//                    }
//
//                    return false;
//                });
//    }
}
