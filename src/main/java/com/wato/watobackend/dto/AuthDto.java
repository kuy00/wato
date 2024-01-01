package com.wato.watobackend.dto;

import lombok.Builder;
import lombok.Data;

import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.Date;

@Data
public class AuthDto {

    private String accessToken;
    private LocalDateTime accessTokenExpiration;

    private String refreshToken;
    private LocalDateTime refreshTokenExpiration;

    @Builder
    public AuthDto(String accessToken, Date accessTokenDate, String refreshToken, Date refreshTokenDate) {
        this.accessToken = accessToken;
        this.accessTokenExpiration = new Timestamp(accessTokenDate.getTime()).toLocalDateTime();
        this.refreshToken = refreshToken;
        this.refreshTokenExpiration = new Timestamp(refreshTokenDate.getTime()).toLocalDateTime();
    }
}
