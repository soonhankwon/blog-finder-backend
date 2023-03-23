package com.soon.service;

public interface SearchService<T> {
    T searchByKakao(String query, String sortType);
    T searchByNaver(String query, String sortType);
}
