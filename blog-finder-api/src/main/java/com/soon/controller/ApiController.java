package com.soon.controller;

import com.soon.domain.SortType;
import com.soon.dto.SearchResultDto;
import com.soon.event.SearchEvent;
import com.soon.service.KeywordSearchServiceRouter;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.context.ApplicationListener;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Mono;

import java.util.List;

@RestController
@RequiredArgsConstructor
@Tag(name = "블로그 검색 API")
public class ApiController {
    private final KeywordSearchServiceRouter keywordSearchServiceRouter;
    private final ApplicationEventPublisher applicationEventPublisher;

    @GetMapping("/search/accuracy")
    @Operation(summary = "카카오 정확도순 블로그 검색 API")
    public Mono<List<SearchResultDto>> apiSearchAccuracy(@RequestParam("query") String query, @RequestParam("sortType") String sortType) {
        applicationEventPublisher.publishEvent(new SearchEvent(this, query));
        return keywordSearchServiceRouter.searchByKakao(query, sortType);
    }

    @GetMapping("/search/recency")
    @Operation(summary = "카카오 최신순 블로그 검색 API")
    public Mono<List<SearchResultDto>> apiSearchRecency(@RequestParam("query") String query, @RequestParam("sortType") String sortType) {
        applicationEventPublisher.publishEvent(new SearchEvent(this, query));
        return keywordSearchServiceRouter.searchByKakao(query, sortType);
    }
}
