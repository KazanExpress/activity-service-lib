package com.kazanexpress.activity.elastic.service;

import com.kazanexpress.activity.model.Record;

public interface PersistenceService {
    /**
     * Persistence service which should process events from {@link com.kazanexpress.activity.annotation.ActivityRecord}
     *
     * @param record which should be stored
     */
    void processRecord(Record record);
}
