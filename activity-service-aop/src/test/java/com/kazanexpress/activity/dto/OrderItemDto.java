package com.kazanexpress.activity.dto;

import com.kazanexpress.activity.annotation.Identifier;
import lombok.Data;

import java.util.Random;

@Data
public class OrderItemDto {

    @Identifier
    private long orderItemId = new Random().nextInt(1000);
}
