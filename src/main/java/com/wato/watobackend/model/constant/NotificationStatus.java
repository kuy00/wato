package com.wato.watobackend.model.constant;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public enum NotificationStatus {
    UNREAD(0, "Unread"),
    READ(1, "Read");

    private int code;
    private String value;
}
