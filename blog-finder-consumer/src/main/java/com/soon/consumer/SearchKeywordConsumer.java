package com.soon.consumer;

import com.soon.service.KeywordService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

@Slf4j
@RequiredArgsConstructor
@Service
public class SearchKeywordConsumer {
    private final KeywordService keywordService;

    @KafkaListener(topics = "keyword", groupId = "keywordStream")
    public void consume(String kafkaMessage) {
        keywordService.collectKeyword(kafkaMessage);
        log.info("Consumed Message : " + kafkaMessage);
    }
}
