package com.wato.watobackend.model.constant;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public enum Role {
    USER(0, "User"),
    ADMIN(1, "Admin");

    private int code;
    private String value;
}
