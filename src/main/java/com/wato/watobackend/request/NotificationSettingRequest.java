package com.wato.watobackend.request;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;

@JsonIgnoreProperties(ignoreUnknown = true)
@Data
public class NotificationSettingRequest {

    private String push;

    private String announcement;

    private String comment;

    private String recommend;
}
