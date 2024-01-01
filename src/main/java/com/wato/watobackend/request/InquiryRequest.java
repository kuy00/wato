package com.wato.watobackend.request;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;

@JsonIgnoreProperties(ignoreUnknown = true)
@Data
public class InquiryRequest {

    private Long id;

    private String title;

    private String content;

    private String email;
}
