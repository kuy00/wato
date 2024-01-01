package com.wato.watobackend.model;

import javax.persistence.Entity;
import javax.persistence.Id;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@NoArgsConstructor
@Setter
@Getter
@Entity
public class RefreshToken {

    @JsonIgnore
    @Id
    private Long userId;

    private String refreshToken;

    private LocalDateTime expiration;

    @Builder
    public RefreshToken(Long userId, String refreshToken, LocalDateTime expiration) {
        this.userId = userId;
        this.refreshToken = refreshToken;
        this.expiration = expiration;
    }
}
