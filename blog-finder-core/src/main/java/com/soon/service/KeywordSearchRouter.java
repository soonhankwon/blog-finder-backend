package com.soon.service;

import com.soon.domain.SortType;
import com.soon.dto.SearchResultDto;
import org.springframework.stereotype.Service;

@Service
public class KeywordSearchRouter implements SearchService<SearchResultDto>{

    @Override
    public SearchResultDto searchByKakao(String query, String sortType) {
        if(sortType.equals(SortType.ACCURACY.getValue())) {
            return new SearchResultDto();
        }
        if(sortType.equals(SortType.RECENCY.getValue())) {
            return new SearchResultDto();
        } else {
            throw new IllegalArgumentException();
        }
    }

    @Override
    public SearchResultDto searchByNaver(String query, String sortType) {
        if(sortType.equals(SortType.SIM.getValue())) {
            return new SearchResultDto();
        }
        if(sortType.equals(SortType.DATE.getValue())) {
            return new SearchResultDto();
        } else {
            throw new IllegalArgumentException();
        }
    }
}
