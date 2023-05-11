package com.soon.controller;

import com.soon.domain.SortType;
import com.soon.dto.SearchResultDto;
import com.soon.service.BlogSearchServiceRouter;
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
@Tag(name = "블로그 검색 API")
public class BlogSearchController {
    private final BlogSearchServiceRouter blogSearchServiceRouter;

    @GetMapping("/blog")
    @Operation(summary = "카카오 정확도순 & 최신순 블로그 검색 API")
    public Mono<List<SearchResultDto>> blogSearchByKeyword(@RequestParam("query") String query, @RequestParam("sortType") SortType sortType) {
        return blogSearchServiceRouter.searchByKakao(query, sortType);
    }
}
