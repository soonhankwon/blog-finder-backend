package com.soon.domain;

import lombok.NoArgsConstructor;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

@Entity
@NoArgsConstructor
public class Keyword {
    private static final long MIN_COUNT = 1;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String word;

    private Long count;

    public Keyword(String word, Long count) {
        if(isWordNullOrEmpty(word)) {
            throw new IllegalArgumentException("검색어는 1글자 이상이어야 합니다.");
        }
        if(!isCountMinimum(count)) {
            throw new IllegalArgumentException();
        }
        this.word = convertUseCaseWord(word);
        this.count = MIN_COUNT;
    }

    private boolean isCountMinimum(Long count) {
        return count >= MIN_COUNT;
    }

    public String getWord() {
        return this.word;
    }

    public Long getCount() {
        return this.count;
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
