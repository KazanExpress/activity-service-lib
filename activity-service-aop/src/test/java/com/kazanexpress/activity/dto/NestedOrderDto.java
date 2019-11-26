package com.kazanexpress.activity.dto;

import com.kazanexpress.activity.annotation.Identifier;
import com.kazanexpress.activity.annotation.NestedIdentifier;
import lombok.Data;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

@Data
public class NestedOrderDto {

    @NestedIdentifier
    List<OrderItemDto> orderItemDto;
    @Identifier("orderId")
    private long id = new Random().nextInt(1000);
    @NestedIdentifier
    private SkuDto skuDto = new SkuDto();

    public NestedOrderDto() {
        orderItemDto = new ArrayList<>();
        orderItemDto.add(new OrderItemDto());
        orderItemDto.add(new OrderItemDto());
    }
}
