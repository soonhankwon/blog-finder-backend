package com.soon.service;

import com.soon.domain.SortType;
import com.soon.dto.SearchResultDto;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Map;
import java.util.function.Function;

@RequiredArgsConstructor
@Service
public class KakaoSearchService implements SearchService {

    @Value("${kakao.api.url}")
    private String kakaoUrl;

    @Value("${kakao.api.path}")
    private String kakaoPath;

    @Value("${kakao.api.key}")
    private String kakaoKey;

    private final int kakaoPagination = 10;

    @Override
    public Mono<List<SearchResultDto>> blogSearchByKeyword(String query, SortType sortType) {
        return WebClient.builder()
                .baseUrl(kakaoUrl)
                .build().get()
                .uri(builder -> builder.path(kakaoPath)
                        .queryParam("query", query)
                        .queryParam("sort", sortType.getValue())
                        .queryParam("page", kakaoPagination)
                        .build())
                .header("Authorization", "KakaoAK " + kakaoKey)
                .retrieve()
                .bodyToMono(Map.class)
                .map(map -> (List<Map>) map.get("documents"))
                .flatMapIterable(Function.identity())
                .map(document -> SearchResultDto.builder()
                        .blogname(document.get("blogname").toString())
                        .title(document.get("title").toString())
                        .contents(document.get("contents").toString())
                        .thumbnail(document.get("thumbnail").toString())
                        .datetime(LocalDateTime.parse(document.get("datetime").toString(), DateTimeFormatter.ISO_DATE_TIME))
                        .url(document.get("url").toString())
                        .build())
                .collectList();
    }
}
