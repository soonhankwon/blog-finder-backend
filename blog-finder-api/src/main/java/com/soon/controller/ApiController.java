package com.soon.controller;

import com.soon.dto.SearchResultDto;
import com.soon.service.KeywordSearchServiceRouter;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Mono;

import java.util.List;

@RestController
@RequiredArgsConstructor
@Tag(name = "카카오 블로그 검색 API")
public class ApiController {
    private final KeywordSearchServiceRouter keywordSearchServiceRouter;

    @GetMapping("/search/accuracy")
    @Operation(summary = "정확도순 블로그 검색 API")
    public Mono<List<SearchResultDto>> apiSearchAccuracy(@RequestParam String query, String sortType) {
        return keywordSearchServiceRouter.searchByKakao(query, sortType);
    }

    @GetMapping("/search/recency")
    @Operation(summary = "최신순 블로그 검색 API")
    public Mono<List<SearchResultDto>> apiSearchRecency(@RequestParam String query, String sortType) {
        return keywordSearchServiceRouter.searchByKakao(query, sortType);
    }
}
