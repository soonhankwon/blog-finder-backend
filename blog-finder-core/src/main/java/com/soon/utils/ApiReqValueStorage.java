package com.soon.utils;

import lombok.Getter;
import org.springframework.beans.factory.annotation.Value;

@Getter
public class ApiReqValueStorage {
    @Value("${kakao.api.url}")
    private String kakaoUrl;

    @Value("${kakao.api.path}")
    private String kakaoPath;

    @Value("${kakao.api.key}")
    private String kakaoKey;

    private int kakaoPagination;

    @Value("${naver.api.url}")
    private String naverUrl;

    @Value("${naver.api.path}")
    private String naverPath;

    @Value("${naver.api.client.id}")
    private String naverClientId;

    @Value("${naver.api.client.secret}")
    private String naverClientSecret;

    private int naverDisplay;
}
