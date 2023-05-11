package com.soon.service;

import com.soon.domain.SortType;
import com.soon.dto.SearchResultDto;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Map;
import java.util.function.Function;

public class NaverBlogSearchService implements BlogSearchService {
    @Value("${naver.api.url}")
    private String naverUrl;

    @Value("${naver.api.path}")
    private String naverPath;

    @Value("${naver.api.client.id}")
    private String naverClientId;

    @Value("${naver.api.client.secret}")
    private String naverClientSecret;

    private final int naverDisplay = 10;

    @Override
    public Mono<List<SearchResultDto>> blogSearchByKeyword(String query, SortType sortType) {
        return WebClient.builder()
                .baseUrl(naverUrl)
                .build().get()
                .uri(uriBuilder -> uriBuilder.path(naverPath)
                        .queryParam("query", query)
                        .queryParam("display", naverDisplay)
                        .queryParam("sort", sortType.getValue())
                        .build())
                .header("X-Naver-Client-Id", naverClientId)
                .header("X-Naver-Client-Secret", naverClientSecret)
                .retrieve()
                .bodyToMono(Map.class)
                .map(map -> (List<Map>) map.get("items"))
                .flatMapIterable(Function.identity())
                .map(item -> SearchResultDto.builder()
                        .blogname(item.get("bloggername").toString())
                        .title(item.get("title").toString())
                        .contents(item.get("description").toString())
                        .datetime(LocalDate.parse(item.get("postdate").toString(), DateTimeFormatter.ofPattern("yyyyMMdd")).atStartOfDay())
                        .url(item.get("link").toString())
                        .build())
                .collectList();
    }
}
