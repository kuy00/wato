package com.wato.watobackend.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.time.LocalDateTime;

@AllArgsConstructor
@Data
public class CommentDto {

    private Long id;

    private Long userId;

    private String nickname;

    private String userImageUrl;

    private String content;

    private LocalDateTime createDate;

    private LocalDateTime updateDate;
}
