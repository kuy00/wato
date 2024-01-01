package com.wato.watobackend.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.wato.watobackend.model.constant.*;
import javax.persistence.*;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@Setter
@Getter
@NoArgsConstructor
@Entity
@Table(name = "user", indexes = {
        @Index(name = "idx_email", columnList = "email"),
        @Index(name = "idx_nickname", columnList = "nickname")
})
public class User extends Date {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private AuthType authType;

    @Column(length = 100)
    private String snsId;

    private String email;

    @JsonIgnore
    private String password;

    @Column(length = 10)
    private String nickname;

    @OneToOne
    private Country country;

    private String gender;

    private String yearOfBirth;

    @Column(length = 20)
    private String job;

    @JsonIgnore
    private String imageUrl;

    private UserStatus status;

    private Role role;

    private LocalDateTime lastLoginTime;

    @Transient
    private String profileImageUrl;

    @Builder
    public User(AuthType authType, String snsId, String email, String password, String nickname,
                Country country, String gender, String yearOfBirth, String job, String imageUrl,
                UserStatus status, Role role, LocalDateTime lastLoginTime) {
        this.authType = authType;
        this.snsId = snsId;
        this.email = email;
        this.password = password;
        this.nickname = nickname;
        this.country = country;
        this.gender = gender;
        this.yearOfBirth = yearOfBirth;
        this.job = job;
        this.imageUrl = imageUrl;
        this.status = status;
        this.role = role;
        this.lastLoginTime = lastLoginTime;
    }
}
