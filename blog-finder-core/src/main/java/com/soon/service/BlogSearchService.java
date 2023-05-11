package com.soon.service;

import com.soon.domain.SortType;
import com.soon.dto.SearchResultDto;
import reactor.core.publisher.Mono;

import java.util.List;

public interface BlogSearchService {
    Mono<List<SearchResultDto>> blogSearchByKeyword(String query, SortType sortType);
}
