package com.wato.watobackend.model.constant;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public enum BannerType {
    NORMALLY(0, "Normally"),
    BAND(1, "Band");

    private int code;
    private String value;
}
