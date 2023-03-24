package com.soon.exception;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
@AllArgsConstructor
public enum ErrorCode {
    SORT_TYPE_INVALID(HttpStatus.BAD_REQUEST, "정렬 방법이 올바르지 않습니다.");

    private final HttpStatus httpStatus;
    private final String message;
}
