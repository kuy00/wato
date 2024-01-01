package com.wato.watobackend.dto;

import lombok.Builder;
import lombok.Data;

@Builder
@Data
public class EmailAuthDto {

    private String email;

    private Integer code;
}
