package com.soon.config;

import com.soon.service.KakaoSearchService;
import com.soon.service.NaverSearchService;
import com.soon.service.SearchService;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class SpringConfig {

    @Bean
    public SearchService searchService() {
        return new KakaoSearchService();
    }

    @Bean
    public SearchService fallBackSearchService() {
        return new NaverSearchService();
    }
}
