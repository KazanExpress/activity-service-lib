package com.kazanexpress.activity.dto;

import com.kazanexpress.activity.annotation.Identifier;
import lombok.Data;

@Data
public class SkuDto {

    @Identifier(exported = false)
    private String skuTitle = "SkuTitleTest";
}
