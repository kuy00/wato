package com.wato.watobackend.controller;

import com.wato.watobackend.exception.ApiException;
import com.wato.watobackend.exception.constant.Error;
import com.wato.watobackend.model.User;
import com.wato.watobackend.request.SignUpRequest;
import com.wato.watobackend.response.ApiResponse;
import com.wato.watobackend.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;

@RequiredArgsConstructor
@RequestMapping("/signup")
@RestController
public class SignUpController {

    private final UserService userService;

    @PostMapping
    public ApiResponse signUp(
            @RequestHeader String authKey,
            @RequestBody(required = false) SignUpRequest request) {
        if (authKey == null) throw new ApiException(Error.AUTH_FAILED);
        if (request == null) throw new ApiException(Error.EMPTY_DATA);
        User user = userService.signUp(authKey, request);
        return ApiResponse.builder().data(user).build();
    }


    @GetMapping("/check/email")
    public ApiResponse checkEmail(@RequestParam(required = false) String email) {
        if (!StringUtils.hasText(email)) throw new ApiException(Error.EMPTY_DATA);
        boolean checkmEmail = userService.checkEmail(email);
        return ApiResponse.builder().data(checkmEmail).build();
    }

    @GetMapping(value = "/check/nickname")
    public ApiResponse checkNickname(
            @RequestParam(required = false) String nickname)
    {
        if (!StringUtils.hasText(nickname)) throw new ApiException(Error.EMPTY_DATA);

        boolean  checkNickname = userService.checkNicknameBySignup(nickname);

        return ApiResponse.builder().data(checkNickname).build();
    }
}