package com.soon.service;

import com.soon.domain.SortType;
import com.soon.dto.SearchResultDto;
import com.soon.utils.ApiReqValueStorage;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.function.Function;

@RequiredArgsConstructor
@Service
public class KeywordSearchService {
    private final ApiReqValueStorage apiReqValueStorage;

    public Mono<List<SearchResultDto>> searchByAccuracy(String query, SortType sortType) {
        if (sortType.getValue().equals(SortType.ACCURACY.getValue())) {
            return kakaoSearchResultToMono(query, sortType);
        }
        if (sortType.getValue().equals(SortType.SIM.getValue())) {
            return naverSearchResultToMono(query, sortType);
        } else {
            throw new IllegalArgumentException();
        }
    }

    public Mono<List<SearchResultDto>> searchByRecency(String query, SortType sortType) {
        if (sortType.getValue().equals(SortType.ACCURACY.getValue())) {
            return kakaoSearchResultToMono(query, sortType);
        }
        if (sortType.getValue().equals(SortType.SIM.getValue())) {
            return naverSearchResultToMono(query, sortType);
        } else {
            throw new IllegalArgumentException();
        }
    }

    private Mono<List<SearchResultDto>> kakaoSearchResultToMono(String query, SortType sortType) {
        return WebClient.builder()
                .baseUrl(apiReqValueStorage.getKakaoUrl())
                .build().get()
                .uri(builder -> builder.path(apiReqValueStorage.getKakaoPath())
                        .queryParam("query", query)
                        .queryParam("sort", sortType.getValue())
                        .queryParam("page", apiReqValueStorage.getKakaoPagination())
                        .build())
                .header("Authorization", "KakaoAK " + apiReqValueStorage.getKakaoKey())
                .retrieve()
                .bodyToMono(Map.class)
                .map(map -> (List<Map>) map.get("documents"))
                .flatMapIterable(Function.identity())
                .map(document -> SearchResultDto.builder()
                        .blogname(document.get("blogname").toString())
                        .title(document.get("title").toString())
                        .contents(document.get("contents").toString())
                        .thumbnail(document.get("title").toString())
                        .datetime(LocalDateTime.parse(document.get("datetime").toString()))
                        .url(document.get("url").toString())
                        .build())
                .collectList();
    }

    private Mono<List<SearchResultDto>> naverSearchResultToMono(String query, SortType sortType) {
        return WebClient.builder()
                .baseUrl(apiReqValueStorage.getNaverUrl())
                .build().get()
                .uri(uriBuilder -> uriBuilder.path(apiReqValueStorage.getNaverPath())
                        .queryParam("query", query)
                        .queryParam("display", apiReqValueStorage.getNaverDisplay())
                        .queryParam("sort", sortType.getValue())
                        .build())
                .header("X-Naver-Client-Id", apiReqValueStorage.getNaverClientId())
                .header("X-Naver-Client-Secret", apiReqValueStorage.getNaverClientSecret())
                .retrieve()
                .bodyToMono(Map.class)
                .map(map -> (List<Map>) map.get("items"))
                .flatMapIterable(Function.identity())
                .map(item -> SearchResultDto.builder()
                        .blogname(item.get("bloggername").toString())
                        .title(item.get("title").toString())
                        .contents(item.get("contents").toString())
                        .datetime(LocalDateTime.parse(item.get("postdate").toString()))
                        .url(item.get("link").toString())
                        .build())
                .collectList();
    }
}
