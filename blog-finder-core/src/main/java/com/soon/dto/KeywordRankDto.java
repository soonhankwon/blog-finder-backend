package com.soon.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class KeywordRankDto {
    private String word;
    private Long count;

    public KeywordRankDto(String word, Long count) {
        this.word = word;
        this.count = count;
    }
}
