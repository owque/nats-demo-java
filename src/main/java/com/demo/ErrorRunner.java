package com.demo;

import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.core.Ordered;
import org.springframework.stereotype.Component;

@Slf4j
//@Component
public class ErrorRunner implements ApplicationRunner, Ordered {
    @Override
    public void run(ApplicationArguments args) {
        log.info("ErrorRunner starting ...");
        // maybe some important resource load failed
       throw new RuntimeException("ErrorRunner ....");
    }

    @Override
    public int getOrder() {
        return 10;
    }
}
