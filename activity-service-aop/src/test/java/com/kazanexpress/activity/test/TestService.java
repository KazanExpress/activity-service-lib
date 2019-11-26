package com.kazanexpress.activity.test;

import com.kazanexpress.activity.annotation.ActivityRecord;
import com.kazanexpress.activity.annotation.Identifier;
import com.kazanexpress.activity.annotation.NestedIdentifier;
import com.kazanexpress.activity.annotation.OrderReceivedRecord;
import com.kazanexpress.activity.dto.InheritedOrderDto;
import com.kazanexpress.activity.dto.NestedOrderDto;
import com.kazanexpress.activity.dto.OrderDto;
import com.kazanexpress.activity.dto.ParamDto;
import org.springframework.stereotype.Component;

@Component
public class TestService {

    public static final String ORDER_DTO_TEST_MESSAGE = "Test message: ${skuTitle}, ${orderId}, ${inheritedField}";
    public static final String INPUT_PARAM_TEST_MESSAGE = "Test message: ${skuTitle}, ${orderId}, ${workerId}";
    public static final String NESTED_ORDER_DTO_TEST_MESSAGE = "Test message: ${orderId}, ${orderItemId}, ${skuTitle}";
    public static final String REQUEST_TEST_MESSAGE = "Test ${testAttribute}, ${orderId}";

    @ActivityRecord(message = ORDER_DTO_TEST_MESSAGE, type = "Test")
    public OrderDto testOrderDto() {
        return new OrderDto();
    }

    @ActivityRecord(message = NESTED_ORDER_DTO_TEST_MESSAGE, type = "Test")
    public NestedOrderDto testNestedOrderDto() {
        return new NestedOrderDto();
    }

    @ActivityRecord(message = INPUT_PARAM_TEST_MESSAGE, type = "Test")
    public OrderDto testInputDtoParam(@NestedIdentifier ParamDto paramDto) {
        return new OrderDto();
    }

    @ActivityRecord(message = INPUT_PARAM_TEST_MESSAGE, type = "Test")
    public OrderDto testInputParam(@Identifier long workerId) {
        return new OrderDto();
    }

    @OrderReceivedRecord
    public OrderDto testInheritedAnnotation(@Identifier long workerId) {
        return new OrderDto();
    }

    @ActivityRecord(message = ORDER_DTO_TEST_MESSAGE, type = "Test")
    public InheritedOrderDto testInheritedIdentifiers() {
        return new InheritedOrderDto();
    }

    @ActivityRecord(message = REQUEST_TEST_MESSAGE, type = "Test",
                    requestParams = "testAttribute",
                    nestedRequestParams = "orderDto")
    public void testRequestContextHolder() {
    }
}
