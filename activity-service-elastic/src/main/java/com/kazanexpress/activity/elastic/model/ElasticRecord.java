package com.kazanexpress.activity.elastic.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.elasticsearch.annotations.DateFormat;
import org.springframework.data.elasticsearch.annotations.Document;
import org.springframework.data.elasticsearch.annotations.Field;
import org.springframework.data.elasticsearch.annotations.FieldType;

import java.util.Date;
import java.util.Map;

@NoArgsConstructor
@AllArgsConstructor
@Builder
@Data
@Document(indexName = "activity-index", type = "record")
public class ElasticRecord {

    @Id
    private String id;
    private String message;
    private String type;

    @Field(type = FieldType.Object, includeInParent = true)
    private Map<String, ?> identifiers;

    @Field(type = FieldType.Date, format = DateFormat.basic_date_time)
    private Date timestamp;
}
