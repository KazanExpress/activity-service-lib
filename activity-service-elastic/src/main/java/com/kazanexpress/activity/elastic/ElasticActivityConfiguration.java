package com.kazanexpress.activity.elastic;

import com.kazanexpress.activity.annotation.EnableActivityService;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableAsync;

@EnableActivityService
@EnableAsync
@EnableAutoConfiguration
@ComponentScan(basePackages = {
        "com.kazanexpress.activity.elastic"
})
@Configuration
public class ElasticActivityConfiguration {
}
