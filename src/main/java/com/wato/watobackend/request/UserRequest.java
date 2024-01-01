package com.wato.watobackend.request;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;

@JsonIgnoreProperties(ignoreUnknown = true)
@Data
public class UserRequest {

    private String nickname;

    private Long country;

    private String gender;

    private String yearOfBirth;

    private String job;
}