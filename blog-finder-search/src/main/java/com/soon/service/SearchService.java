package com.soon.service;

public interface SearchService<T> {
    T blogSearchByKeyword(String query, String sortType);
}
