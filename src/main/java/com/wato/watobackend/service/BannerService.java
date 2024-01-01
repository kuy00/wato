package com.wato.watobackend.service;

import com.wato.watobackend.dto.BannerDto;
import com.wato.watobackend.exception.ApiException;
import com.wato.watobackend.exception.constant.Error;
import com.wato.watobackend.model.constant.BannerType;
import com.wato.watobackend.repository.BannerRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.util.List;
import java.util.Optional;

@RequiredArgsConstructor
@Service
public class BannerService {

    private final BannerRepository bannerRepository;

    public List<BannerDto> getBanners(BannerType type) {
        List<BannerDto> banners = bannerRepository.findAllByType(type);
        for (BannerDto banner : banners) {
            if (StringUtils.hasText(banner.getImageUrl())) {
                String imageUrl = ServletUriComponentsBuilder.fromCurrentContextPath().path(banner.getImageUrl()).toUriString();
                banner.setWebImageUrl(imageUrl);
                banner.setMobileImageUrl(imageUrl.replace("_web", "_mo"));
            }
        }

        return banners;
    }

    public BannerDto getBanner(Long id) {
        Optional<BannerDto> optBanner = bannerRepository.findDtoById(id);
        if (optBanner.isEmpty()) throw new ApiException(Error.NOT_EXIST_BANNER);

        if (StringUtils.hasText(optBanner.get().getImageUrl())) {
            String imageUrl = ServletUriComponentsBuilder.fromCurrentContextPath().path(optBanner.get().getImageUrl()).toUriString();
            optBanner.get().setWebImageUrl(imageUrl);
            optBanner.get().setMobileImageUrl(imageUrl.replace("_web", "_mo"));
        }

        return optBanner.get();
    }
}
