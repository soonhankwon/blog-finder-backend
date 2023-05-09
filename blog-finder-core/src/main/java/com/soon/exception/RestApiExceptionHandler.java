package com.soon.exception;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class RestApiExceptionHandler {

    @ExceptionHandler(value = {ApiException.class})
    public ResponseEntity<ErrorResponseDto> handleApiRequestException(ApiException e) {
        return ResponseEntity.status(e.getHttpStatus())
                .body(new ErrorResponseDto(e.getHttpStatus(), e.getMessage()));
    }
}
