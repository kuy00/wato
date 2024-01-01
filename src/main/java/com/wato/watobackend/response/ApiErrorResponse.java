package com.wato.watobackend.response;

import com.wato.watobackend.exception.ApiException;
import com.wato.watobackend.exception.constant.Error;
import lombok.Builder;
import lombok.Data;

@Data
public class ApiErrorResponse {

    private Integer code;

    private String message;

    @Builder
    public ApiErrorResponse(ApiException error) {
        this.code = error.getCode();
        this.message = error.getMessage();
    }

    public ApiErrorResponse(Error error) {
        this.code = error.getCode();
        this.message = error.getMessage();
    }

    public ApiErrorResponse(String message) {
        this.code = Error.INVALID_DATA.getCode();
        this.message = message;
    }
}