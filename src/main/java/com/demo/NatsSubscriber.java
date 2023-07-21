package com.demo;

import io.nats.client.Connection;
import io.nats.client.Dispatcher;
import io.nats.client.Nats;
import io.nats.client.Options;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.core.Ordered;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.io.UnsupportedEncodingException;

@Slf4j
//@Component
public class NatsSubscriber implements ApplicationRunner, Ordered {

    @Override
    public void run(ApplicationArguments args) throws IOException, InterruptedException {
        log.info("NatsSubscriber starting ...");
        Options options = new Options.Builder().build();
        Connection connection = Nats.connect(options);
        Dispatcher dispatcher = connection.createDispatcher();
        dispatcher.subscribe("demo-subject-1", (msg) -> {
            try {
                log.info("get Nats message {}", new String(msg.getData(), "utf-8"));
            } catch (UnsupportedEncodingException e) {
                log.error("handle Nats message error", e);
            }
        });
        log.info("NatsSubscriber started");
    }

    @Override
    public int getOrder() {
        return 1;
    }
}
