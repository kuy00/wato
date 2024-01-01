package com.wato.watobackend.request;

import lombok.Data;

import javax.validation.constraints.NotEmpty;

@Data
public class ReportRequest {

    @NotEmpty
    private String reason;
}
