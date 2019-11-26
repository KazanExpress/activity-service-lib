package com.kazanexpress.activity.dto;

import com.kazanexpress.activity.annotation.Identifier;
import lombok.Data;

import java.util.Random;

@Data
public class ParamDto {

    @Identifier
    private long workerId = new Random().nextInt(1000);

    @Identifier(exported = false)
    private String skuTitle = "SkuTitle2";
}
