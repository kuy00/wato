package com.wato.watobackend.request;


import lombok.Builder;
import lombok.Data;

@Builder
@Data
public class EmailRequest {

    private String to; //수신

    private String subject;

    private String message;
}
