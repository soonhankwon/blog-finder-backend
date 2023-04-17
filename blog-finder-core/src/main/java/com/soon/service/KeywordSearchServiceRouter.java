package com.soon.service;

import com.soon.domain.SortType;
import com.soon.dto.SearchResultDto;
import com.soon.exception.ErrorCode;
import com.soon.exception.RequestException;
import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import reactor.core.publisher.Mono;

import java.util.List;

@Component
@RequiredArgsConstructor
public class KeywordSearchServiceRouter {
    private final List<SearchService> searchServices;
    private static final String BREAKER = "breaker";

    @CircuitBreaker(name = BREAKER, fallbackMethod = "searchByNaver")
    @Transactional(readOnly = true)
    public Mono<List<SearchResultDto>> searchByKakao(String query, SortType sortType) {
        sortType.validSortType();
        return findSearchService(KakaoSearchService.class)
                .blogSearchByKeyword(query, sortType);
    }

    @Transactional(readOnly = true)
    public Mono<List<SearchResultDto>> searchByNaver(String query, SortType sortType, RuntimeException e) {
        sortType.validSortType();
        return findSearchService(NaverSearchService.class)
                .blogSearchByKeyword(query, sortType.convertSortTypeForNaver());
    }

    private SearchService findSearchService(Class<? extends SearchService> serviceClass) {
        return searchServices.stream()
                .filter(e -> e.getClass().equals(serviceClass))
                .findFirst()
                .orElseThrow(() -> new RequestException(ErrorCode.SERVICE_NOT_FOUND));
    }
}
