package com.kazanexpress.activity.config;

import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableAsync;

@EnableAsync
@ComponentScan(basePackages = {
        "com.kazanexpress.activity.aspect",
        "com.kazanexpress.activity.service",
})
@Configuration
public class ActivityConfiguration {
}
