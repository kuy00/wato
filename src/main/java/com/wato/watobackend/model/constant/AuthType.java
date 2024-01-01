package com.wato.watobackend.model.constant;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public enum AuthType {

    EMAIL(0, "Email"),
    FACEBOOK(1, "Facebook"),
    GOOGLE(2, "Google"),
    NAVER(3, "Naver"),
    KAKAO(4, "Kakao");

    private int code;
    private String value;
}
