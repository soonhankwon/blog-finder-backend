package com.soon.controller;

import com.soon.dto.KeywordRankDto;
import com.soon.service.KeywordService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequiredArgsConstructor
@Tag(name = "인기 검색어 TOP10 API")
public class KeywordRankController {
    private final KeywordService keywordService;

    @GetMapping("/keyword/rank")
    @Operation(summary = "인기 검색어 TOP10 과 인기 검색어 별 검색횟수 API")
    public List<KeywordRankDto> getKeywordRankAndCount() {
        return keywordService.getTop10KewordsAndCount();
    }
}
