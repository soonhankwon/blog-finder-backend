package com.soon.event;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.ApplicationListener;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;
import org.springframework.transaction.event.TransactionPhase;
import org.springframework.transaction.event.TransactionalEventListener;

@Slf4j
@Component
@RequiredArgsConstructor
public class SearchEventListener implements ApplicationListener<SearchEvent> {

    private final KafkaTemplate<String, String> kafkaTemplate;

    @Override
    @Async
    @TransactionalEventListener(phase = TransactionPhase.BEFORE_COMMIT)
    public void onApplicationEvent(SearchEvent event) {
        String keyword = event.getKeyword();
        String topic = "keyword";
        kafkaTemplate.send(topic, keyword, keyword);
        log.info("Kafka Producer sent data : " + keyword);
    }
}

