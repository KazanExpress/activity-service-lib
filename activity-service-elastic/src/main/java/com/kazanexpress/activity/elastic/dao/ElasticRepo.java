package com.kazanexpress.activity.elastic.dao;

import com.kazanexpress.activity.elastic.model.ElasticRecord;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ElasticRepo extends ElasticsearchRepository<ElasticRecord, String> {
}
