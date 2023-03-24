package com.soon.service;

import com.soon.domain.SortType;
import com.soon.dto.SearchResultDto;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Mono;

import java.util.List;

@Component
@RequiredArgsConstructor
public class KeywordSearchServiceRouter implements SearchServiceRouter<Mono<List<SearchResultDto>>>{

    private final KeywordSearchService keywordSearchService;

    @Override
    public Mono<List<SearchResultDto>> searchByKakao(String query, String sortType) {
        if(sortType.equals(SortType.ACCURACY.getValue())) {
            return keywordSearchService.searchByAccuracy(query, SortType.ACCURACY);
        }
        if(sortType.equals(SortType.RECENCY.getValue())) {
            return keywordSearchService.searchByRecency(query, SortType.RECENCY);
        } else {
            throw new IllegalArgumentException();
        }
    }

    @Override
    public Mono<List<SearchResultDto>> searchByNaver(String query, String sortType) {
        if(sortType.equals(SortType.SIM.getValue())) {
            return keywordSearchService.searchByAccuracy(query, SortType.SIM);
        }
        if(sortType.equals(SortType.DATE.getValue())) {
            return keywordSearchService.searchByRecency(query, SortType.DATE);
        } else {
            throw new IllegalArgumentException();
        }
    }
}
