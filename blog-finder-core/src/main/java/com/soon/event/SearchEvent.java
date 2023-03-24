package com.soon.event;

import org.springframework.context.ApplicationEvent;

public class SearchEvent extends ApplicationEvent {
    private final String keyword;

    public SearchEvent(Object source, String keyword) {
        super(source);
        this.keyword = keyword;
    }

    public String getKeyword() {
        return this.keyword;
    }
}
