package com.wato.watobackend.response;

import lombok.Builder;
import lombok.Data;

@Data
public class ApiResponse {

    private Object data;

    private int code;

    private String message;

    @Builder
    public ApiResponse(Object data) {
        if (data != null) this.data = data;
        this.code = 0;
        this.message = "Success";
    }
}
