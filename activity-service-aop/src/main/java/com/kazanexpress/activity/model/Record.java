package com.kazanexpress.activity.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.util.Date;
import java.util.Map;
import java.util.UUID;

@NoArgsConstructor
@AllArgsConstructor
@Builder
@Data
public class Record {

    @Builder.Default
    @ToString.Exclude
    private String id = UUID.randomUUID().toString();
    private String message;
    private String type;
    private Map<String, ?> identifiers;
    @Builder.Default
    private Date timestamp = new Date();
}
