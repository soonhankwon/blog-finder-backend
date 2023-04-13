package com.soon.service;

import com.soon.domain.SortType;
import com.soon.dto.SearchResultDto;
import com.soon.exception.ErrorCode;
import com.soon.exception.RequestException;
import com.soon.utils.ApiReqValueStorage;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Map;
import java.util.function.Function;

@RequiredArgsConstructor
@Service
public class NaverSearchService implements SearchService<Mono<List<SearchResultDto>>> {
    private final ApiReqValueStorage apiReqValueStorage;

    @Override
    public Mono<List<SearchResultDto>> search(String query, String sortType) {
        return searchResultToMono(query, SortType.valueOf(sortType.toUpperCase()));
    }

    private Mono<List<SearchResultDto>> searchResultToMono(String query, SortType sortType) {
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
                        .contents(item.get("description").toString())
                        .datetime(LocalDate.parse(item.get("postdate").toString(), DateTimeFormatter.ofPattern("yyyyMMdd")).atStartOfDay())
                        .url(item.get("link").toString())
                        .build())
                .collectList();
    }
}
