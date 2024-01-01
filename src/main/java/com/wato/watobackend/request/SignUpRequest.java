package com.wato.watobackend.request;

import lombok.Data;

import javax.validation.constraints.NotEmpty;

@Data
public class SignUpRequest {

    @NotEmpty
    private String email;

    @NotEmpty
    private String password;

    private String nickname;

    private Long country;

    private String gender;

    private String yearOfBirth;

    private String job;

    private String imageUrl;
}