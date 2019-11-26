package com.kazanexpress.activity.elastic.service.impl;

import com.kazanexpress.activity.elastic.dao.ElasticRepo;
import com.kazanexpress.activity.elastic.model.ElasticRecord;
import com.kazanexpress.activity.elastic.service.PersistenceService;
import com.kazanexpress.activity.model.Record;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.event.EventListener;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import java.util.HashMap;

@RequiredArgsConstructor
@Slf4j
@Service
public class ElasticPersistenceServiceImpl implements PersistenceService {

    private final ElasticRepo elasticRepo;

    @Async
    @EventListener
    @Override
    public void processRecord(Record record) {
        ElasticRecord elasticRecord = ElasticRecord.builder()
                .id(record.getId())
                .identifiers(new HashMap<>(record.getIdentifiers()))
                .message(record.getMessage())
                .timestamp(record.getTimestamp())
                .build();
        log.debug("Send to Elastic...");
        elasticRepo.save(elasticRecord);
        log.debug("Done: {}", elasticRepo.findById(elasticRecord.getId()));
    }
}
