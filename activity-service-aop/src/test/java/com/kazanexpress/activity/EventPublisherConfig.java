package com.kazanexpress.activity;

import org.mockito.Mockito;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Primary;
import org.springframework.context.support.GenericApplicationContext;

@TestConfiguration
public class EventPublisherConfig {

    @Bean
    @Primary
    ApplicationEventPublisher genericApplicationContext(final GenericApplicationContext gac) {
        return Mockito.spy(gac);
    }
}
