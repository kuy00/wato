package com.wato.watobackend.model.constant;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public enum NotificationType {
    ANNOUNCEMENT(0, "새소식", "'{title}'새소식"),
    COMMENT(1, "새 댓글", "'{title}'게시글에 '댓글'이 달렸습니다"),
    RECOMMEND(2, "새 추천", "'{title}'새 추천 게시글");

    private int code;
    private String value;
    private String message;
}
