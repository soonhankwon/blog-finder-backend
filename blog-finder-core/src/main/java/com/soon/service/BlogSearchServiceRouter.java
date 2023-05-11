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
public class BlogSearchServiceRouter {
    private final BlogSearchService blogSearchService;
    private final BlogSearchService fallBackBlogSearchService;

    private static final String BREAKER = "breaker";

    @CircuitBreaker(name = BREAKER, fallbackMethod = "searchByNaver")
    @Transactional(readOnly = true)
    public Mono<List<SearchResultDto>> searchByKakao(String query, SortType sortType) {
        sortType.validSortType();
        return blogSearchService.blogSearchByKeyword(query, sortType);
    }

    @Transactional(readOnly = true)
    public Mono<List<SearchResultDto>> searchByNaver(String query, SortType sortType, RuntimeException e) {
        sortType.validSortType();
        return fallBackBlogSearchService.blogSearchByKeyword(query, sortType.convertSortTypeForNaver());
    }
}