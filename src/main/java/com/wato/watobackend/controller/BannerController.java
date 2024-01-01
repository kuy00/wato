package com.wato.watobackend.controller;

import com.wato.watobackend.dto.BannerDto;
import com.wato.watobackend.response.ApiResponse;
import com.wato.watobackend.service.BannerService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RequiredArgsConstructor
@RequestMapping("/banner")
@RestController
public class BannerController {

    private final BannerService bannerService;

    @GetMapping("/{id}")
    public ApiResponse getBanner(@PathVariable Long id) {
        BannerDto banner = bannerService.getBanner(id);

        return ApiResponse.builder().data(banner).build();
    }
}
