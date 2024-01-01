package com.wato.watobackend.dto;

import com.wato.watobackend.model.Country;
import com.wato.watobackend.model.constant.AuthType;
import lombok.Data;

@Data
public class UserDto {
    private AuthType authType;
    private String snsId;
    private String email;
    private String password;
    private String nickname;
    private Country country;
}
