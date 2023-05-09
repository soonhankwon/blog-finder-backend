package com.soon.exception;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
@AllArgsConstructor
public class ApiException extends RuntimeException {
    private final HttpStatus httpStatus;
    private final String message;

    public ApiException(ErrorCode errorCode) {
        this.httpStatus = errorCode.getHttpStatus();
        this.message = errorCode.getMessage();
    }
}
