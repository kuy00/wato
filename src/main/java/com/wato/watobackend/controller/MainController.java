package com.wato.watobackend.controller;

import com.wato.watobackend.dto.BannerDto;
import com.wato.watobackend.dto.PageDto;
import com.wato.watobackend.model.Category;
import com.wato.watobackend.model.constant.BannerType;
import com.wato.watobackend.response.ApiResponse;
import com.wato.watobackend.service.BannerService;
import com.wato.watobackend.service.CategoryService;
import com.wato.watobackend.service.PostService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RequiredArgsConstructor
@RequestMapping("/main")
@RestController
public class MainController {

    private final PostService postService;
    private final CategoryService categoryService;
    private final BannerService bannerService;

    @GetMapping("/post")
    public ApiResponse getPosts(
            @RequestParam(defaultValue = "top")  String filter,
            @RequestParam(defaultValue = "1") int page,
            @RequestParam(defaultValue = "10") int size
    ) {
        if (page > 0) {
            page = page -1;
        } else {
            page = 0;
        }
        PageDto posts = postService.getPosts(filter, page, size);

        return ApiResponse.builder().data(posts).build();
    }

    @GetMapping("/category")
    public ApiResponse getCategories() {
        List<Category> categories = categoryService.getCategories();

        return ApiResponse.builder().data(categories).build();
    }

    @GetMapping("/banner")
    public ApiResponse getBanners() {
        List<BannerDto> banners = bannerService.getBanners(BannerType.NORMALLY);

        return ApiResponse.builder().data(banners).build();
    }

    @GetMapping("/banner/band")
    public ApiResponse getBandBanners() {
        List<BannerDto> banners = bannerService.getBanners(BannerType.BAND);

        return ApiResponse.builder().data(banners).build();
    }
}
