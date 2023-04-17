package com.soon.service;

import com.soon.dto.SearchResultDto;
import reactor.core.publisher.Mono;

import java.util.List;

public interface SearchService {
    Mono<List<SearchResultDto>> blogSearchByKeyword(String query, String sortType);
}
