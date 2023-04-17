package com.soon.service;

import com.soon.domain.SortType;
import com.soon.dto.SearchResultDto;
import com.soon.exception.ErrorCode;
import com.soon.exception.RequestException;
import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import reactor.core.publisher.Mono;

import java.util.List;

@Slf4j
@Component
@RequiredArgsConstructor
public class KeywordSearchServiceRouter implements SearchServiceSelector {
    private final List<SearchService> searchServices;
    private static final String BREAKER = "breaker";

    @CircuitBreaker(name = BREAKER, fallbackMethod = "searchByNaver")
    @Transactional(readOnly = true)
    public Mono<List<SearchResultDto>> searchByKakao(String query, String sortType) {
        if (!isSortTypeValid(sortType)) {
            throw new RequestException(ErrorCode.SORT_TYPE_INVALID);
        } else {
            return findSearchService(KakaoSearchService.class)
                    .blogSearchByKeyword(query, sortType);
        }
    }

    @Transactional(readOnly = true)
    public Mono<List<SearchResultDto>> searchByNaver(String query, String sortType, RuntimeException e) {
        if (!isSortTypeValid(sortType)) {
            throw new RequestException(ErrorCode.SORT_TYPE_INVALID);
        } else {
            return findSearchService(NaverSearchService.class)
                    .blogSearchByKeyword(query, convertSortTypeForNaver(sortType));
        }
    }

    @Override
    public SearchService findSearchService(Class<? extends SearchService> serviceClass) {
        return searchServices.stream()
                .filter(e -> e.getClass().equals(serviceClass))
                .findFirst()
                .orElseThrow(RuntimeException::new);
    }

    private boolean isSortTypeValid(String sortType) {
        return (sortType.equals(SortType.ACCURACY.getValue()) || sortType.equals(SortType.RECENCY.getValue()));
    }

    private String convertSortTypeForNaver(String sortType) {
        if (sortType.equals(SortType.ACCURACY.getValue())) {
            return SortType.SIM.getValue();
        } else {
            return SortType.DATE.getValue();
        }
    }
}
