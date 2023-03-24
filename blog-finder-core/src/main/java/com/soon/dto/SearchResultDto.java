package com.soon.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;

import java.time.LocalDateTime;

@Builder
@AllArgsConstructor
public class SearchResultDto {
    private String blogname;
    private String contents;
    private String title;
    private String url;
    private LocalDateTime datetime;
    private String thumbnail;
}
