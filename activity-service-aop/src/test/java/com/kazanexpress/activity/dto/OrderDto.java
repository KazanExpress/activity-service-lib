package com.kazanexpress.activity.dto;

import com.kazanexpress.activity.annotation.Identifier;
import lombok.Data;

@Data
public class OrderDto {

    @Identifier
    private Long orderId = 10L;

    @Identifier(exported = false)
    private String skuTitle = "SkuTitleTest";

    private String notUsedField = "Field";
}
