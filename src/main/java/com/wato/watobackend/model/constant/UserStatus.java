package com.wato.watobackend.model.constant;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public enum UserStatus {

    NORMALLY(0, "Normally"), // 일반
    DELETED(1, "Deleted"), // 삭제
    WITHDRAWN(2, "Withdrawn"); // 탈퇴

    private int code;
    private String value;
}
