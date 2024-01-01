package com.wato.watobackend.dto;

import lombok.Builder;
import lombok.Data;

import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.Date;

@Data
public class TokenDto {

    private String token;
    private LocalDateTime Expiration;

    @Builder
    public TokenDto(String token, Date expiration) {
        this.token = token;
        this.Expiration = new Timestamp(expiration.getTime()).toLocalDateTime();
    }
}
