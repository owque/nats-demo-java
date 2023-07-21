package com.demo;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.core.Ordered;
import org.springframework.stereotype.Component;

import java.io.IOException;

@Slf4j
@Component
public class PubsubSubscriber implements ApplicationRunner, Ordered {
    @Autowired
    private PubSubComponent pubSubComponent;

    @Override
    public void run(ApplicationArguments args) {
        log.info("PubsubSubscriber starting ...");
        String projectId = "ow-dummy-topic-2023";
        String topicId = "ow-dummy-topic-2023";
        String subscription = "ow-dummy-topic-2023-sub";
        try {
            pubSubComponent.pullMessage(projectId, topicId, subscription, (message, consumer) -> {
                log.info("get Pub/Sub message: {}", message.getData().toStringUtf8());
            });
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        log.info("PubsubSubscriber started");
    }

    @Override
    public int getOrder() {
        return 2;
    }
}
