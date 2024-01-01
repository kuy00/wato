package com.wato.watobackend.controller;

import com.wato.watobackend.dto.PageDto;
import com.wato.watobackend.response.ApiResponse;
import com.wato.watobackend.service.NotificationService;
import com.wato.watobackend.service.PostService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RequiredArgsConstructor
@RequestMapping("/notification")
@RestController
public class NotificationController {

    private final NotificationService notificationService;

    @GetMapping
    public ApiResponse getNotification(
            Authentication authentication,
            @RequestParam(defaultValue = "1") int page,
            @RequestParam(defaultValue = "10") int size
    ) {
        UserDetails userDetails = (UserDetails) authentication.getPrincipal();
        if (page > 0) {
            page = page -1;
        } else {
            page = 0;
        }
        PageDto notifications = notificationService.getNotification(Long.parseLong(userDetails.getUsername()), page, size);

        return ApiResponse.builder().data(notifications).build();
    }
}
