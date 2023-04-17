package com.soon.service;

@FunctionalInterface
public interface SearchServiceSelector {
    SearchService findSearchService(Class<? extends SearchService> serviceClass);
}
