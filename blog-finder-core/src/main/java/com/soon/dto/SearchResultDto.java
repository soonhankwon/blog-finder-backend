package com.soon.dto;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;

@Builder
@Getter
@AllArgsConstructor
public class SearchResultDto {
    private String blogname;

    private String title;

    private String contents;

    private String url;

    private LocalDateTime datetime;

    private String thumbnail;
}
