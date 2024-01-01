package com.wato.watobackend.dto;

import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;

@Data
public class AuthCodeDto {

    private String token;

    private LocalDateTime Expiration;

    @Builder
    public AuthCodeDto(String token, LocalDateTime expiration) {
        this.token = token;
        this.Expiration = expiration;
    }
}
