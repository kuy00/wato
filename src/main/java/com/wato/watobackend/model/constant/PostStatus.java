package com.wato.watobackend.model.constant;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public enum PostStatus {
    ENABLED(0, "Enabled"), // 게시중
    DELETED(1, "Deleted"), // 삭제
    DISABLED(2, "Disabled"); // 비노출

    private int code;
    private String value;
}
