package com.soon.utils;

import lombok.Getter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Getter
@Component
public class ApiReqValueStorage {
    @Value("${kakao.api.url}")
    private String kakaoUrl;

    @Value("${kakao.api.path}")
    private String kakaoPath;

    @Value("${kakao.api.key}")
    private String kakaoKey;

    private final int kakaoPagination = 10;

    @Value("${naver.api.url}")
    private String naverUrl;

    @Value("${naver.api.path}")
    private String naverPath;

    @Value("${naver.api.client.id}")
    private String naverClientId;

    @Value("${naver.api.client.secret}")
    private String naverClientSecret;

    private final int naverDisplay = 10;
}
