package com.wato.watobackend.dto;

import lombok.Builder;
import lombok.Data;

@Builder
@Data
public class PushDto {

    private String fcmToken;

    private String linkUrl;

    private String message;
}
