package com.soon.domain;

import com.soon.exception.ErrorCode;
import com.soon.exception.RequestException;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Getter
@Entity
@NoArgsConstructor
@Table(name = "keyword", indexes = {
        @Index(name = "idx_word", columnList = "word"),
        @Index(name = "idx_count", columnList = "count")})
public class Keyword {
    public static final long MIN_COUNT = 1;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String word;

    private Long count;

    public Keyword(String word, Long count) {
        if(isWordNullOrEmpty(word)) {
            throw new RequestException(ErrorCode.KEYWORD_INVALID);
        }
        if(!isCountMinimum(count)) {
            throw new RequestException(ErrorCode.SEARCH_COUNT_INVALID);
        }
        this.word = convertUseCaseWord(word);
        this.count = MIN_COUNT;
    }

    public Keyword(String word) {
        this.word = convertUseCaseWord(word);
    }

    private boolean isCountMinimum(Long count) {
        return count >= MIN_COUNT;
    }

    private String convertUseCaseWord(String word) {
        return removeSpecialChar(word).toLowerCase();
    }

    private String removeSpecialChar(String word) {
        return word.replaceAll("[^ㄱ-ㅎㅏ-ㅣ가-힣a-zA-Z0-9]","");
    }

    public void increaseCount() {
        this.count++;
    }

    private boolean isWordNullOrEmpty(String word) {
        return word == null || word.isEmpty();
    }
}
