package com.wato.watobackend.controller;

import com.wato.watobackend.dto.AuthCodeDto;
import com.wato.watobackend.exception.ApiException;
import com.wato.watobackend.exception.constant.Error;
import com.wato.watobackend.model.Inquiry;
import com.wato.watobackend.request.AuthEmailRequest;
import com.wato.watobackend.request.LoginRequest;
import com.wato.watobackend.response.ApiResponse;
import com.wato.watobackend.service.AuthenticationService;
import com.wato.watobackend.service.NotificationService;
import com.wato.watobackend.util.CookieUtil;
import com.wato.watobackend.util.RequestUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import static org.springframework.security.oauth2.core.endpoint.OAuth2ParameterNames.ACCESS_TOKEN;
import static org.springframework.security.oauth2.core.endpoint.OAuth2ParameterNames.REFRESH_TOKEN;

@RequiredArgsConstructor
@RequestMapping("/auth")
@RestController
public class AuthController {

    private final AuthenticationService authenticationService;
    private final NotificationService notificationService;

    @PostMapping("/login")
    public ApiResponse login(@RequestBody LoginRequest request) {
        if (request == null) throw new ApiException(Error.EMPTY_DATA);

        if (!StringUtils.hasText(request.getEmail()) || !StringUtils.hasText(request.getPassword())) {
            throw new ApiException(Error.INVALID_DATA);
        }

        AuthCodeDto code = authenticationService.login(request);
        return ApiResponse.builder().data(code).build();
    }

    @PostMapping("/login/email")
    public ApiResponse authEmailByLogin(
            HttpServletRequest request,
            HttpServletResponse response,
            @RequestHeader String authKey,
            @RequestBody AuthEmailRequest requestBody)
    {
        if (requestBody == null) throw new ApiException(Error.EMPTY_DATA);

        if (!StringUtils.hasText(authKey) || requestBody.getCode() == null) {
            throw new ApiException(Error.INVALID_DATA);
        }

        boolean isAuth = authenticationService.authEmailByLogin(request, response, authKey, requestBody);
        return ApiResponse.builder().data(isAuth).build();
    }

    @GetMapping("/signup/email")
    public ApiResponse sendAuthEmailCodeBySignup(
            @RequestParam(required = false) String email)
    {
        if (!StringUtils.hasText(email)) throw new ApiException(Error.EMPTY_PARAM);

        AuthCodeDto code = authenticationService.sendAuthEmailCodeBySignup(email);
        return ApiResponse.builder().data(code).build();
    }

    @PostMapping("/signup/email")
    public ApiResponse authEmailBySignup(
            @RequestHeader String authKey,
            @RequestBody AuthEmailRequest requestBody)
    {
        if (requestBody == null) throw new ApiException(Error.EMPTY_DATA);

        if (!StringUtils.hasText(authKey) || requestBody.getCode() == null) {
            throw new ApiException(Error.INVALID_DATA);
        }

        boolean isAuth = authenticationService.authEmailBySignup(authKey, requestBody);
        return ApiResponse.builder().data(isAuth).build();
    }

    @PostMapping("/refresh")
    public ApiResponse refreshToken(
            HttpServletRequest request,
            HttpServletResponse response) {
        String token = RequestUtil.getAccessToken(request);

        boolean isAuth = authenticationService.refreshToken(request, response, token);
        return ApiResponse.builder().data(isAuth).build();
    }

    @PostMapping("/logout")
    public ApiResponse logout(
            HttpServletRequest request,
            HttpServletResponse response,
            Authentication authentication
    ) {
        UserDetails userDetails = (UserDetails) authentication.getPrincipal();

        CookieUtil.deleteCookie(request, response, ACCESS_TOKEN);
        CookieUtil.deleteCookie(request, response, REFRESH_TOKEN);

        boolean logout = notificationService.deleteFcmToken(Long.parseLong(userDetails.getUsername()));

        return ApiResponse.builder().data(logout).build();
    }
}
