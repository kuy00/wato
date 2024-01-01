package com.wato.watobackend.request;

import lombok.Data;

import javax.validation.constraints.NotEmpty;

@Data
public class FcmRequest {

    @NotEmpty
    private String fcmToken;
}
