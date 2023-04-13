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

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Map;
import java.util.function.Function;

@RequiredArgsConstructor
@Service
public class KakaoSearchService implements SearchService<Mono<List<SearchResultDto>>> {
    private final ApiReqValueStorage apiReqValueStorage;

    @Override
    public Mono<List<SearchResultDto>> search(String query, String sortType) {
        SortType type = SortType.valueOf(sortType.toUpperCase());
        if (!isSortTypeValid(type)) {
            throw new RequestException(ErrorCode.SORT_TYPE_INVALID);
        }
        return searchResultToMono(query, type);
    }

    private boolean isSortTypeValid(SortType type) {
        return type.equals(SortType.ACCURACY) || type.equals(SortType.RECENCY);
    }

    private Mono<List<SearchResultDto>> searchResultToMono(String query, SortType sortType) {
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
                        .thumbnail(document.get("thumbnail").toString())
                        .datetime(LocalDateTime.parse(document.get("datetime").toString(), DateTimeFormatter.ISO_DATE_TIME))
                        .url(document.get("url").toString())
                        .build())
                .collectList();
    }
}
