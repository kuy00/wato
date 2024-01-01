package com.wato.watobackend.exception;

import com.wato.watobackend.exception.constant.Error;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ApiException extends RuntimeException {

    private int code;

    private String message;

    public Error error;

    public ApiException(Error error) {
        this.error = error;
        this.code = error.getCode();
        this.message = error.getMessage();
    }
}
