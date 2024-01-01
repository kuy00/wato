package com.wato.watobackend.controller;

import com.wato.watobackend.dto.PageDto;
import com.wato.watobackend.response.ApiResponse;
import com.wato.watobackend.service.PostService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RequiredArgsConstructor
@RequestMapping("/recommend")
@RestController
public class RecommendController {

    private final PostService postService;

    @GetMapping("/category")
    public ApiResponse getRecommendByCategory(
            @RequestParam(defaultValue = "1") int page,
            @RequestParam(defaultValue = "10") int size
    ) {
        if (page > 0) {
            page = page -1;
        } else {
            page = 0;
        }
        PageDto recommends = postService.getRecommendByCategory(page, size);

        return ApiResponse.builder().data(recommends).build();
    }

    @GetMapping("/country")
    public ApiResponse getRecommendByCountry(
            @RequestParam(defaultValue = "1") int page,
            @RequestParam(defaultValue = "10") int size
    ) {
        if (page > 0) {
            page = page -1;
        } else {
            page = 0;
        }
        PageDto recommends = postService.getRecommendByCountry(page, size);

        return ApiResponse.builder().data(recommends).build();
    }
}
