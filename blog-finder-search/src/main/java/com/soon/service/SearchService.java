package com.soon.service;

public interface SearchService<T> {
    T search(String query, String sortType);
}
