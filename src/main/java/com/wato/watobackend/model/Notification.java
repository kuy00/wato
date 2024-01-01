package com.wato.watobackend.model;

import com.wato.watobackend.model.constant.Date;
import com.wato.watobackend.model.constant.NotificationStatus;
import com.wato.watobackend.model.constant.NotificationType;
import javax.persistence.*;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Setter
@Getter
@NoArgsConstructor
@Entity
public class Notification extends Date {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    private User receiver;

    @ManyToOne
    private User actor;

    private NotificationType type;

    private Long contentId;

    private String content;

    private String linkUrl;

    private NotificationStatus status;

    @Builder
    public Notification(User receiver, User actor, NotificationType type, Long contentId, String content, String linkUrl) {
        this.receiver = receiver;
        this.actor = actor;
        this.type = type;
        this.contentId = contentId;
        this.content = content;
        this.linkUrl = linkUrl;
        this.status = NotificationStatus.UNREAD;
    }
}
