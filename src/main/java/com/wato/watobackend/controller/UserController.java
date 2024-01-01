package com.wato.watobackend.controller;

import com.wato.watobackend.dto.PageDto;
import com.wato.watobackend.exception.ApiException;
import com.wato.watobackend.exception.constant.Error;
import com.wato.watobackend.model.FcmToken;
import com.wato.watobackend.model.NotificationSetting;
import com.wato.watobackend.model.User;
import com.wato.watobackend.request.*;
import com.wato.watobackend.response.ApiResponse;
import com.wato.watobackend.service.NotificationSettingService;
import com.wato.watobackend.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.validation.Valid;

@RequiredArgsConstructor
@RequestMapping("/user")
@RestController
public class UserController {

    private final UserService userService;
    private final NotificationSettingService settingService;

    @GetMapping("")
    public ApiResponse getUsers(
            Authentication authentication,
            @RequestParam(defaultValue = "1") int page,
            @RequestParam(defaultValue = "10") int size
    ) {
        try {
            page = page > 0 ? page - 1 : 0;
            UserDetails userDetails = (UserDetails) authentication.getPrincipal();
            PageDto users = userService.getUsers(Long.parseLong(userDetails.getUsername()), page, size);

            return ApiResponse.builder().data(users).build();
        } catch (ApiException e) {
            throw new ApiException(e.error);
        } catch (Exception e) {
            throw new ApiException(Error.SERVER_ERROR);
        }
    }

    @GetMapping("/profile")
    public ApiResponse getProfile(
            Authentication authentication
    ) {
        UserDetails userDetails = (UserDetails) authentication.getPrincipal();
        User user = userService.getUser(Long.parseLong(userDetails.getUsername()));

        return ApiResponse.builder().data(user).build();
    }

    @PutMapping(value = "/profile")
    public ApiResponse updateProfile(
            Authentication authentication,
            @RequestPart(value = "request", required = false) UserRequest request,
            @RequestPart(value = "file", required = false) MultipartFile file)
    {
        UserDetails userDetails = (UserDetails) authentication.getPrincipal();
        User user = userService.updateProfile(Long.parseLong(userDetails.getUsername()), request, file);

        return ApiResponse.builder().data(user).build();
    }

    @GetMapping(value = "/check/nickname")
    public ApiResponse checkNickname(
            Authentication authentication,
            @RequestParam(required = false) String nickname)
    {
        if (!StringUtils.hasText(nickname)) throw new ApiException(Error.EMPTY_PARAM);

        UserDetails userDetails = (UserDetails) authentication.getPrincipal();
        boolean  checkNickname = userService.checkNicknameByProfile(Long.parseLong(userDetails.getUsername()), nickname);

        return ApiResponse.builder().data(checkNickname).build();
    }

    @PutMapping("/fcm/token")
    public ApiResponse updateFcmToken(
            Authentication authentication,
            @RequestBody FcmRequest request)
    {
        if (!StringUtils.hasText(request.getFcmToken())) throw new ApiException(Error.EMPTY_DATA);

        UserDetails userDetails = (UserDetails) authentication.getPrincipal();
        FcmToken fcmToken = settingService.updateFcmToken(Long.parseLong(userDetails.getUsername()), request);

        return ApiResponse.builder().data(fcmToken).build();
    }

    @GetMapping("/notification/setting")
    public ApiResponse getNotificationSetting(
            Authentication authentication
    ){
        UserDetails userDetails = (UserDetails) authentication.getPrincipal();
        NotificationSetting setting = settingService.getNotificationSetting(Long.parseLong(userDetails.getUsername()));

        return ApiResponse.builder().data(setting).build();
    }

    @PutMapping("/notification/setting")
    public ApiResponse updateNotificationSetting(
            Authentication authentication,
            @RequestBody NotificationSettingRequest request) {
        UserDetails userDetails = (UserDetails) authentication.getPrincipal();
        NotificationSetting setting = settingService.updateNotificationSetting(Long.parseLong(userDetails.getUsername()), request);

        return ApiResponse.builder().data(setting).build();
    }

    @PostMapping("/block")
    public ApiResponse blockUser(
            Authentication authentication,
            @Valid @RequestBody(required=false) TargetRequest request) {
        if (request == null || request.getUserId() == null) throw new ApiException(Error.EMPTY_DATA);
        UserDetails userDetails = (UserDetails) authentication.getPrincipal();
        boolean blockUser = userService.blockUser(Long.parseLong(userDetails.getUsername()), request);

        return ApiResponse.builder().data(blockUser).build();
    }

    @DeleteMapping("/block")
    public ApiResponse deleteBlockUser(
            Authentication authentication,
            @Valid @RequestBody(required=false) TargetRequest request) {
        UserDetails userDetails = (UserDetails) authentication.getPrincipal();
        boolean deleteBlockUser = userService.deleteBlockUser(Long.parseLong(userDetails.getUsername()), request);

        return ApiResponse.builder().data(deleteBlockUser).build();
    }
}
