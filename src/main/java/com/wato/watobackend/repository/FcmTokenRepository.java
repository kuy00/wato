package com.wato.watobackend.repository;

import com.wato.watobackend.model.FcmToken;
import org.springframework.data.jpa.repository.JpaRepository;

public interface FcmTokenRepository extends JpaRepository<FcmToken, Long> {

    FcmToken findByUserId(Long userId);
}
