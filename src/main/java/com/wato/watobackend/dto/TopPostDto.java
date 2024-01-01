package com.wato.watobackend.dto;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.time.LocalDateTime;

@AllArgsConstructor
@Data
public class TopPostDto {

    private Long id;

    private Long countryId;

    private String countryName;

    private Long categoryId;

    private String categoryName;

    private String title;

    private String content;

    private Long userId;

    private String nickname;

    @JsonIgnore
    private String userImageUrl;

    private String profileImageUrl;

    @JsonIgnore
    private String imageUrl;

    private String postImageUrl;

    private Integer commentCount;

    private LocalDateTime createDate;

    private LocalDateTime updateDate;

    public TopPostDto(Long id, Long countryId, String countryName, Long categoryId, String categoryName, String title, String content, Long userId, String nickname, String userImageUrl, String imageUrl, Integer commentCount, LocalDateTime createDate, LocalDateTime updateDate) {
        this.id = id;
        this.countryId = countryId;
        this.countryName = countryName;
        this.categoryId = categoryId;
        this.categoryName = categoryName;
        this.title = title;
        this.content = content;
        this.userId = userId;
        this.nickname = nickname;
        this.userImageUrl = userImageUrl;
        this.imageUrl = imageUrl;
        this.commentCount = commentCount;
        this.createDate = createDate;
        this.updateDate = updateDate;
    }
}
