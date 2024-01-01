package com.wato.watobackend.service;

import com.wato.watobackend.exception.ApiException;
import com.wato.watobackend.exception.constant.Error;
import com.wato.watobackend.model.FcmToken;
import com.wato.watobackend.model.NotificationSetting;
import com.wato.watobackend.model.constant.SettingType;
import com.wato.watobackend.repository.FcmTokenRepository;
import com.wato.watobackend.repository.NotificationSettingRepository;
import com.wato.watobackend.request.FcmRequest;
import com.wato.watobackend.request.NotificationSettingRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Optional;

@RequiredArgsConstructor
@Service
public class NotificationSettingService {

    private final NotificationSettingRepository settingRepository;
    private final FcmTokenRepository fcmTokenRepository;

    public NotificationSetting getNotificationSetting(Long userId) {
        Optional<NotificationSetting> optSetting = settingRepository.findById(userId);
        if (optSetting.isEmpty()) throw new ApiException(Error.NOT_EXIST_SETTING);

        return optSetting.get();
    }

    public void createNotificationSetting(Long userId) {
        NotificationSetting notificationSetting = NotificationSetting.builder().userId(userId).build();
        settingRepository.save(notificationSetting);
    }

    public NotificationSetting updateNotificationSetting(Long userId, NotificationSettingRequest request) {
        NotificationSetting setting = getNotificationSetting(userId);
        setting.setPush(checkSetting(request.getPush()));
        setting.setAnnouncement(checkSetting(request.getAnnouncement()));
        setting.setComment(checkSetting(request.getComment()));
        setting.setRecommend(checkSetting(request.getRecommend()));

        return settingRepository.save(setting);
    }

    private static SettingType checkSetting(String setting) {
        return setting.equals(SettingType.ON.getValue()) ? SettingType.ON : SettingType.OFF;
    }

    public FcmToken updateFcmToken(Long userId, FcmRequest request) {
        FcmToken fcmToken = fcmTokenRepository.findByUserId(userId);
        if (fcmToken == null) {
            fcmToken = FcmToken.builder()
                    .userId(userId).build();
        }
        fcmToken.setFcmToken(request.getFcmToken());

        return fcmTokenRepository.save(fcmToken);
    }
}
