package com.soon.exception;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
@AllArgsConstructor
public class ErrorResponseDto {
    private HttpStatus httpStatus;
    private String message;
}
