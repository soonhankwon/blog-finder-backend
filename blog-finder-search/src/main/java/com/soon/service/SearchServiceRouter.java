package com.soon.service;

public interface SearchServiceRouter<T> {
    T searchByKakao(String query, String sortType);
    T searchByNaver(String query, String sortType, RuntimeException e);
}
