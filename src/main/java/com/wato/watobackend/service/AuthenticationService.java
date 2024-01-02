package com.wato.watobackend.service;

import com.wato.watobackend.dto.AuthCodeDto;
import com.wato.watobackend.dto.EmailAuthDto;
import com.wato.watobackend.dto.TokenDto;
import com.wato.watobackend.exception.ApiException;
import com.wato.watobackend.exception.constant.Error;
import com.wato.watobackend.model.RefreshToken;
import com.wato.watobackend.model.User;
import com.wato.watobackend.repository.RefreshTokenRepository;
import com.wato.watobackend.repository.UserRepository;
import com.wato.watobackend.request.AuthEmailRequest;
import com.wato.watobackend.request.LoginRequest;
import com.wato.watobackend.security.JwtProvider;
import com.wato.watobackend.util.CookieUtil;
import io.jsonwebtoken.Claims;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.time.LocalDateTime;
import java.util.Date;
import java.util.Optional;
import java.util.Random;

import static org.springframework.security.oauth2.core.endpoint.OAuth2ParameterNames.ACCESS_TOKEN;
import static org.springframework.security.oauth2.core.endpoint.OAuth2ParameterNames.REFRESH_TOKEN;

@RequiredArgsConstructor
@Service
public class AuthenticationService {

    private final EmailService emailService;
    private final UserRepository userRepository;

    private final PasswordEncoder passwordEncoder;
    private final JwtProvider jwtProvider;

    @Value("${jwt.accessToken.duration}")
    public Long jwtAccessTokenDuration;

    public AuthCodeDto login(LoginRequest request) {
        Optional<User> optUser = userRepository.findByEmail(request.getEmail());
        if (optUser.isEmpty()) throw new ApiException(Error.NOT_EXIST_USER);

        if (StringUtils.hasText(optUser.get().getSnsId())) {
            throw new ApiException(Error.SNS_LOGIN_EMAIL);
        }

        if (!passwordEncoder.matches(request.getPassword(), optUser.get().getPassword())) {
            throw new ApiException(Error.AUTH_FAILED);
        }

        TokenDto token = jwtProvider.generateToken(optUser.get().getId(), optUser.get().getEmail());

        return AuthCodeDto.builder().token(token.getToken()).expiration(token.getExpiration()).build();
    }

    private static Integer getAuthCode() {
        Random random = new Random();
        Integer code = 100000 + random.nextInt(900000);
        return code;
    }

    public boolean authEmailBySignup(
            String authKey,
            AuthEmailRequest requestBody)
    {
        EmailAuthDto emailAuthDto = jwtProvider.getEmailAuthentication(authKey);

        Optional<User> optUser = userRepository.findByEmail(emailAuthDto.getEmail());
        if (optUser.isPresent()) throw new ApiException(Error.EMAIL_ALREADY_USED);

        if (!String.valueOf(emailAuthDto.getCode()).equals(String.valueOf(requestBody.getCode()))) {
            throw new ApiException(Error.AUTH_FAILED);
        }

        return true;
    }

    public boolean authEmailByLogin(
            HttpServletRequest httpServletRequest,
            HttpServletResponse httpServletResponse,
            String authKey,
            AuthEmailRequest requestBody)
    {
        EmailAuthDto emailAuthDto = jwtProvider.getEmailAuthentication(authKey);

        Optional<User> optUser = userRepository.findByEmail(emailAuthDto.getEmail());
        if (optUser.isEmpty()) throw new ApiException(Error.NOT_EXIST_USER);

        if (!String.valueOf(emailAuthDto.getCode()).equals(String.valueOf(requestBody.getCode()))) throw new ApiException(Error.AUTH_FAILED);

        TokenDto accessTokenDto = jwtProvider.generateToken(optUser.get().getId(), optUser.get().getEmail());

        CookieUtil.deleteCookie(httpServletRequest, httpServletResponse, ACCESS_TOKEN);
        CookieUtil.addCookie(httpServletResponse, ACCESS_TOKEN, accessTokenDto.getToken(), jwtAccessTokenDuration.intValue() / 1000);

        optUser.get().setLastLoginTime(LocalDateTime.now());
        userRepository.save(optUser.get());

        return true;
    }

    public AuthCodeDto sendAuthEmailCodeBySignup(String email) {
        Optional<User> optUser = userRepository.findByEmail(email);
        if (optUser.isPresent()) throw new ApiException(Error.EMAIL_ALREADY_USED);

        Integer code = getAuthCode();

        TokenDto token = jwtProvider.generateTokenByCode(Long.valueOf(0), email, code);
        emailService.send(email, "Wato 인증코드입니다.", code);

        return AuthCodeDto.builder().token(token.getToken()).expiration(token.getExpiration()).build();
    }
}
