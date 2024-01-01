package com.wato.watobackend.service;

import com.wato.watobackend.dto.PageDto;
import com.wato.watobackend.model.FcmToken;
import com.wato.watobackend.model.Notification;
import com.wato.watobackend.repository.FcmTokenRepository;
import com.wato.watobackend.repository.NotificationRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import javax.annotation.PostConstruct;
import java.util.concurrent.ConcurrentHashMap;

@Slf4j
@RequiredArgsConstructor
@Service
public class NotificationService {

    private final ConcurrentHashMap<Long, String> fcmTokenMap = new ConcurrentHashMap<>();

    private final PostService postService;
    private final CommentService commentService;
    private final NotificationSettingService notificationSettingService;
    private final NotificationRepository notificationRepository;
    private final FcmTokenRepository fcmTokenRepository;

    @PostConstruct
    public void initFcmToken() {
        for (FcmToken fcmToken : fcmTokenRepository.findAll()) {
            fcmTokenMap.put(fcmToken.getUserId(), fcmToken.getFcmToken());
        }
        log.info("initFcmToken");
    }

    public PageDto getNotification(Long userId, int page, int size) {
        Page<Notification> notifications = notificationRepository.findByReceiverId(userId, PageRequest.of(page, size));
        return null;
    }

    public boolean deleteFcmToken(Long userId) {
        notificationRepository.deleteById(userId);
        String fcmToken = fcmTokenMap.get(userId);
        if (StringUtils.hasText(fcmToken)) {
            fcmTokenMap.remove(userId);
            log.info("logout userId({}) : fcmToken({}) delete", userId, fcmToken);
        }

        return true;
    }
}
