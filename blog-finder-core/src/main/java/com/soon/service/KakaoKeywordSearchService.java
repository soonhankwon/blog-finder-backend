package com.soon.service;

import com.soon.dto.SearchResultDto;
import org.springframework.stereotype.Service;

@Service
public class KakaoKeywordSearchService implements SearchService<SearchResultDto>{

    @Override
    public SearchResultDto searchByAccuracy(String query) {
        return null;
    }

    @Override
    public SearchResultDto searchByRecency(String query) {
        return null;
    }
}
