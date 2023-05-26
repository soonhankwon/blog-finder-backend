package com.soon.aop;

import com.soon.domain.Keyword;
import com.soon.domain.SortType;
import com.soon.event.SearchEvent;
import lombok.RequiredArgsConstructor;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Component;

@Aspect
@Component
@RequiredArgsConstructor
public class SearchEventAspect {
    private final ApplicationEventPublisher applicationEventPublisher;

    @Before(value = "execution(* com.soon.controller.BlogSearchController.blogSearchByKeyword(..)) && args(query,sortType)", argNames = "query,sortType")
    public void beforeBlogSearch(String query, SortType sortType) {
        applicationEventPublisher.publishEvent(new SearchEvent(this, new Keyword(query)));
    }
}
