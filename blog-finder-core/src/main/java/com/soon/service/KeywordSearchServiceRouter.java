package com.soon.service;

import com.soon.domain.SortType;
import com.soon.dto.SearchResultDto;
import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import reactor.core.publisher.Mono;

import java.util.List;

@Component
@RequiredArgsConstructor
public class KeywordSearchServiceRouter {
    private final SearchService kakaoSearchService;
    private final SearchService naverSearchService;
    private static final String BREAKER = "breaker";

    @CircuitBreaker(name = BREAKER, fallbackMethod = "searchByNaver")
    @Transactional(readOnly = true)
    public Mono<List<SearchResultDto>> searchByKakao(String query, SortType sortType) {
        sortType.validSortType();
        return kakaoSearchService.blogSearchByKeyword(query, sortType);
    }

    @Transactional(readOnly = true)
    public Mono<List<SearchResultDto>> searchByNaver(String query, SortType sortType, RuntimeException e) {
        sortType.validSortType();
        return naverSearchService.blogSearchByKeyword(query, sortType.convertSortTypeForNaver());
    }
}
