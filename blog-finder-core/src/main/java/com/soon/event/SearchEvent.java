package com.soon.event;

import com.soon.domain.Keyword;
import org.springframework.context.ApplicationEvent;

public class SearchEvent extends ApplicationEvent {
    private final Keyword keyword;

    public SearchEvent(Object source, Keyword keyword) {
        super(source);
        this.keyword = keyword;
    }

    public String getWord() {
        return this.keyword.getWord();
    }
}
