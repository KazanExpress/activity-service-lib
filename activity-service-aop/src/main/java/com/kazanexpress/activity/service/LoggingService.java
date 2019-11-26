package com.kazanexpress.activity.service;

import com.kazanexpress.activity.model.Record;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.event.EventListener;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

@Slf4j
@Service
public class LoggingService {
    @Async
    @EventListener
    public void logRecord(Record record) {
        log.debug("Record value: {}", record);
    }
}
