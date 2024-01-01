package com.wato.watobackend.exception;

import com.wato.watobackend.exception.constant.Error;
import com.wato.watobackend.response.ApiErrorResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.HttpMediaTypeNotSupportedException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import javax.servlet.http.HttpServletRequest;
import java.util.stream.Collectors;


@Slf4j
@RequiredArgsConstructor
@ControllerAdvice
public class ApiExceptionHandler {

    @ExceptionHandler(Exception.class)
    public ResponseEntity handleException(HttpServletRequest request, ApiException error) {
        log.error("ApiException message({}:{}), path({} {})", error.getCode(), error.getMessage(), request.getMethod(), request.getRequestURI());

        return ResponseEntity.ok(ApiErrorResponse.builder().error(error).build());
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity MethodArgumentNotValidException(HttpServletRequest request, MethodArgumentNotValidException error) {
        String filed = error.getBindingResult().getFieldErrors().stream().map(FieldError::getField).collect(Collectors.joining(","));
        log.error("MethodArgumentNotValidException message({}), filed({}), path({} {})", error.getMessage(), filed, request.getMethod(), request.getRequestURI());

        String message = Error.INVALID_DATA.getMessage() + " : " + filed;
        return ResponseEntity.ok().body(new ApiErrorResponse(message));
    }

    @ExceptionHandler(HttpMediaTypeNotSupportedException.class)
    public ResponseEntity HttpMediaTypeNotSupportedException(HttpServletRequest request, HttpMediaTypeNotSupportedException error) {
        log.error("HttpMediaTypeNotSupportedException message({}), type({}), path({} {})", error.getMessage(), error.getContentType(), request.getMethod(), request.getRequestURI());

        String message = Error.INVALID_DATA.getMessage() + " 타입: " + error.getContentType();
        return ResponseEntity.ok().body(new ApiErrorResponse(message));
    }
}
