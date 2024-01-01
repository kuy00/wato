package com.wato.watobackend.repository;

import com.wato.watobackend.model.Notification;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.jpa.repository.JpaRepository;

public interface NotificationRepository extends JpaRepository<Notification, Long> {

    Page<Notification> findByReceiverId(Long userId, PageRequest pageRequest);
}
