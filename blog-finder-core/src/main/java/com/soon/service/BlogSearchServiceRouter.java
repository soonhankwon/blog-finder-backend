package com.soon.service;

import com.soon.domain.SortType;
import com.soon.dto.SearchResultDto;
import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import reactor.core.publisher.Mono;

import java.util.List;
import java.util.Map;

@Component
public class BlogSearchServiceRouter {

    private final Map<String, BlogSearchService> blogSearchServiceMap;

    public BlogSearchServiceRouter(Map<String, BlogSearchService> blogSearchServiceMap) {
        this.blogSearchServiceMap = blogSearchServiceMap;
    }

    private static final String BREAKER = "breaker";

    @CircuitBreaker(name = BREAKER, fallbackMethod = "searchByNaver")
    @Transactional(readOnly = true)
    public Mono<List<SearchResultDto>> searchByKakao(String query, SortType sortType) {
        sortType.validSortType();
        return blogSearchServiceMap.get("kakaoBlogSearchService")
                .blogSearchByKeyword(query, sortType);
    }

    @Transactional(readOnly = true)
    public Mono<List<SearchResultDto>> searchByNaver(String query, SortType sortType, RuntimeException e) {
        sortType.validSortType();
        return blogSearchServiceMap.get("naverBlogSearchService")
                .blogSearchByKeyword(query, sortType.convertSortTypeForNaver());
    }
}
