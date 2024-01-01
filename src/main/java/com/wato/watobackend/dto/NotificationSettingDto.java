package com.wato.watobackend.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.wato.watobackend.model.NotificationSetting;
import lombok.Data;

@JsonInclude(JsonInclude.Include.NON_NULL)
@Data
public class NotificationSettingDto {

    private String push;

    private String announcement;

    private String comment;

    private String recommend;

    public NotificationSettingDto(String push, String announcement, String comment, String recommend) {
        this.push = push;
        this.announcement = announcement;
        this.comment = comment;
        this.recommend = recommend;
    }
}
