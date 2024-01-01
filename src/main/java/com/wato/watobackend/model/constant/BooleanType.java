package com.wato.watobackend.model.constant;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public enum BooleanType {
    FALSE(0, false),
    TRUE(1, true);

    private int code;
    private Boolean value;
}
