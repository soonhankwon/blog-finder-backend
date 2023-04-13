package com.soon.domain;

import com.soon.dto.KeywordRankDto;
import com.soon.exception.ErrorCode;
import com.soon.exception.RequestException;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.Objects;

@Entity
@NoArgsConstructor
@AllArgsConstructor
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

    public String getWord() {
        return this.word;
    }

    public KeywordRankDto createKeywordRankDto() {
        return new KeywordRankDto(this.word, this.count);
    }

    public void increaseCount() {
        this.count++;
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

    private boolean isWordNullOrEmpty(String word) {
        return word == null || word.isEmpty();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Keyword keyword = (Keyword) o;
        return Objects.equals(id, keyword.id) && Objects.equals(word, keyword.word) && Objects.equals(count, keyword.count);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, word, count);
    }
}
