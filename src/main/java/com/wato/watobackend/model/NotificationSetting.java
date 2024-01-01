package com.wato.watobackend.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.wato.watobackend.model.constant.BooleanType;
import com.wato.watobackend.model.constant.SettingType;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.Entity;
import javax.persistence.Id;

@NoArgsConstructor
@Setter
@Getter
@Entity
public class NotificationSetting {

    @JsonIgnore
    @Id
    private Long userId;

    private SettingType push;

    private SettingType announcement;

    private SettingType comment;

    private SettingType recommend;

    @Builder
    public NotificationSetting(Long userId) {
        this.userId = userId;
        this.push = SettingType.ON;
        this.announcement = SettingType.ON;
        this.comment = SettingType.ON;
        this.recommend = SettingType.ON;
    }
}
