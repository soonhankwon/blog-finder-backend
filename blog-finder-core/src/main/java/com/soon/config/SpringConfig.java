package com.soon.config;

import com.soon.service.KakaoBlogSearchService;
import com.soon.service.NaverBlogSearchService;
import com.soon.service.BlogSearchService;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class SpringConfig {

    @Bean
    public BlogSearchService searchService() {
        return new KakaoBlogSearchService();
    }

    @Bean
    public BlogSearchService fallBackSearchService() {
        return new NaverBlogSearchService();
    }
}
