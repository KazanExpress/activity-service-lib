package com.kazanexpress.activity.dto;

import com.kazanexpress.activity.annotation.Identifier;
import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = true)
public class InheritedOrderDto extends OrderDto {

    @Identifier
    private String inheritedField = "inherited value";
}
