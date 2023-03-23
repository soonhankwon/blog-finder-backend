package com.soon.service;

public interface SearchService<T> {
    T searchByAccuracy(String query);
    T searchByRecency(String query);
}
