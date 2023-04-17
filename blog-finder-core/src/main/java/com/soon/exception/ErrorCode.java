package com.soon.exception;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
@AllArgsConstructor
public enum ErrorCode {
    SORT_TYPE_INVALID(HttpStatus.BAD_REQUEST, "정렬 방법이 올바르지 않습니다."),
    KEYWORD_INVALID(HttpStatus.BAD_REQUEST, "키워드는 1자리 이상이어야 합니다."),
    SEARCH_COUNT_INVALID(HttpStatus.BAD_REQUEST, "유효하지 않은 카운트입니다."),
    SERVICE_NOT_FOUND(HttpStatus.NOT_FOUND, "서비스가 구현되어 있지 않습니다." );

    private final HttpStatus httpStatus;
    private final String message;
}
