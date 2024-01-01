package com.wato.watobackend.model.constant;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public enum SettingType {
    OFF(0, "OFF"),
    ON(1, "ON");

    private int code;
    private String value;
}
