package com.wato.watobackend.repository;

import com.wato.watobackend.model.RefreshToken;
import org.springframework.data.jpa.repository.JpaRepository;

public interface RefreshTokenRepository extends JpaRepository<RefreshToken, Long> {

    RefreshToken findByUserId(Long userId);

    RefreshToken findByUserIdAndRefreshToken(Long userId, String refreshToken);
}
