package com.wato.watobackend.dto;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.wato.watobackend.model.constant.BannerType;
import lombok.Data;

import java.time.LocalDateTime;

@Data
public class BannerDto {

    private Long id;

    private BannerType type;

    private String title;

    @JsonIgnore
    private String imageUrl;

    private String mobileImageUrl;

    private String webImageUrl;

    private String linkUrl;

    private LocalDateTime createDate;

    private LocalDateTime updateDate;

    public BannerDto(Long id, BannerType type, String title, String imageUrl, String linkUrl, LocalDateTime createDate, LocalDateTime updateDate) {
        this.id = id;
        this.type = type;
        this.title = title;
        this.imageUrl = imageUrl;
        this.linkUrl = linkUrl;
        this.createDate = createDate;
        this.updateDate = updateDate;

    }
}
