package com.wato.watobackend.security.oauth2;

import com.wato.watobackend.exception.ApiException;
import com.wato.watobackend.exception.constant.Error;
import com.wato.watobackend.model.User;
import com.wato.watobackend.model.constant.AuthType;
import com.wato.watobackend.model.constant.Role;
import com.wato.watobackend.model.constant.UserStatus;
import com.wato.watobackend.repository.UserRepository;
import com.wato.watobackend.security.UserPrincipal;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.InternalAuthenticationServiceException;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.oidc.user.OidcUser;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Collection;
import java.util.Optional;

@Slf4j
@RequiredArgsConstructor
@Service
public class CustomOAuth2UserService extends DefaultOAuth2UserService {

    private final UserRepository userRepository;

    @Override
    public OAuth2User loadUser(OAuth2UserRequest userRequest) throws OAuth2AuthenticationException {
        OAuth2User user = super.loadUser(userRequest);

        try {
            return this.process(userRequest, user);
        } catch (AuthenticationException ex) {
            throw ex;
        } catch (Exception ex) {
            ex.printStackTrace();
            throw new InternalAuthenticationServiceException(ex.getMessage(), ex.getCause());
        }
    }

    private OAuth2User process(OAuth2UserRequest userRequest, OAuth2User oAuth2User) {
        AuthType authType = AuthType.valueOf(userRequest.getClientRegistration().getRegistrationId().toUpperCase());

        OAuth2UserInfo userInfo = OAuth2UserInfoFactory.getOAuth2UserInfo(authType, oAuth2User.getAttributes());

        System.out.println(userInfo.getId());
        System.out.println(userInfo.getName());
        System.out.println(userInfo.getEmail());
        System.out.println(userInfo.getAttributes());

        User user = findBySnsId(userInfo.getId());
        if (user != null) {
            if (authType != user.getAuthType()) {
                throw new ApiException(Error.AUTH_TYPE_MISMATCH);
            }
            user.setLastLoginTime(LocalDateTime.now());
        } else {
            user = signUpOauth2(userInfo, authType);
        }

        Collection<? extends GrantedAuthority> authorities = oAuth2User.getAuthorities();
        if (authorities != null) {
            Role role = hasAuthority(authorities, Role.ADMIN.getValue()) ? Role.ADMIN : Role.USER;
            user.setRole(role);
        }
        user = userRepository.save(user);

        return UserPrincipal.builder()
                .user(user)
                .attributes(oAuth2User.getAttributes())
                .build();
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

    public User findBySnsId(String snsId) {
        Optional<User> optUser = userRepository.findBySnsId(snsId);

        return optUser.isPresent() ? optUser.get() : null;
    }

    public User signUpOauth2(OAuth2UserInfo userInfo, AuthType authType) {
        User user = User.builder()
                .snsId(userInfo.getId())
                .email(userInfo.getEmail())
                .authType(authType)
                .status(UserStatus.NORMALLY)
                .lastLoginTime(LocalDateTime.now())
                .build();

        return user;
    }
}
