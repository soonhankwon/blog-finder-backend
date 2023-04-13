package com.soon.service;

import com.soon.domain.Keyword;
import com.soon.dto.KeywordRankDto;
import com.soon.repsoitory.KeywordRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@Service
public class KeywordService {
    private final KeywordRepository keywordRepository;

    @Transactional
    public void collectKeyword(String kafkaMessage) {
        if(!keywordRepository.existsKeywordByWord(kafkaMessage)) {
            keywordRepository.save(new Keyword(kafkaMessage, Keyword.MIN_COUNT));
        } else {
            Keyword keyword = keywordRepository.findKeywordByWord(kafkaMessage);
            keyword.increaseCount();
        }
    }

    @Transactional(readOnly = true)
    public List<KeywordRankDto> getTop10KewordsAndCount() {
        List<Keyword> top10Keywords = keywordRepository.findTop10ByOrderByCountDesc();
        return top10Keywords.stream()
                .map(keyword -> keyword.createKeywordRankDto())
                .collect(Collectors.toList());
    }
}
