package com.kazanexpress.activity;

import com.kazanexpress.activity.dto.InheritedOrderDto;
import com.kazanexpress.activity.dto.NestedOrderDto;
import com.kazanexpress.activity.dto.OrderDto;
import com.kazanexpress.activity.dto.OrderItemDto;
import com.kazanexpress.activity.dto.ParamDto;
import com.kazanexpress.activity.model.Record;
import com.kazanexpress.activity.test.TestService;
import org.apache.commons.text.StringSubstitutor;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.ArgumentCaptor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.ConfigFileApplicationContextInitializer;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.HashMap;
import java.util.List;
import java.util.Random;
import java.util.stream.Collectors;

import static com.kazanexpress.activity.test.TestService.INPUT_PARAM_TEST_MESSAGE;
import static com.kazanexpress.activity.test.TestService.NESTED_ORDER_DTO_TEST_MESSAGE;
import static com.kazanexpress.activity.test.TestService.ORDER_DTO_TEST_MESSAGE;
import static java.util.Collections.singletonList;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.mockito.Mockito.clearInvocations;
import static org.mockito.Mockito.verify;

@RunWith(SpringRunner.class)
@ContextConfiguration(classes = EventPublisherConfig.class,
                      initializers = ConfigFileApplicationContextInitializer.class)
@SpringBootTest(classes = Application.class)
public class TestAspectProcessor {

    @Autowired
    ApplicationEventPublisher eventPublisher;

    @Autowired
    TestService testService;

    @Before
    public void setUp() {
        clearInvocations(eventPublisher);
    }

    @Test
    public void testSimpleAspectProcessing() {
        ArgumentCaptor<Record> recordArgumentCaptor = ArgumentCaptor.forClass(Record.class);

        OrderDto orderDto = testService.testOrderDto();

        HashMap<String, Object> valueMap = new HashMap<>();
        valueMap.put("orderId", orderDto.getOrderId());
        valueMap.put("skuTitle", orderDto.getSkuTitle());
        String refinedMessage = StringSubstitutor.replace(ORDER_DTO_TEST_MESSAGE, valueMap);

        verify(eventPublisher).publishEvent(recordArgumentCaptor.capture());

        Record record = recordArgumentCaptor.getValue();
        assertEquals(refinedMessage, record.getMessage());
        assertEquals(singletonList(orderDto.getOrderId()), record.getIdentifiers().get("orderId"));
        assertNull(record.getIdentifiers().get("skuTitle"));
        assertNotNull(record.getId());
    }

    @Test
    public void testInheritedObjectsAspectProcessing() {
        ArgumentCaptor<Record> recordArgumentCaptor = ArgumentCaptor.forClass(Record.class);

        InheritedOrderDto orderDto = testService.testInheritedIdentifiers();

        HashMap<String, Object> valueMap = new HashMap<>();
        valueMap.put("orderId", orderDto.getOrderId());
        valueMap.put("skuTitle", orderDto.getSkuTitle());
        valueMap.put("inheritedField", orderDto.getInheritedField());
        String refinedMessage = StringSubstitutor.replace(ORDER_DTO_TEST_MESSAGE, valueMap);

        verify(eventPublisher).publishEvent(recordArgumentCaptor.capture());

        Record record = recordArgumentCaptor.getValue();
        assertEquals(refinedMessage, record.getMessage());
        assertEquals(singletonList(orderDto.getOrderId()), record.getIdentifiers().get("orderId"));
        assertEquals(singletonList(orderDto.getInheritedField()), record.getIdentifiers().get("inheritedField"));
        assertNull(record.getIdentifiers().get("skuTitle"));
        assertNotNull(record.getId());
    }

//    @Test
//    public void testRequestContextHolderAspectProcessing() {
//        ArgumentCaptor<Record> recordArgumentCaptor = ArgumentCaptor.forClass(Record.class);
//        RequestContextHolder.setRequestAttributes(new ServletRequestAttributes(new MockHttpServletRequest()));
//
//        String testAttributeValue = "value";
//        OrderDto orderDto = new OrderDto();
//        RequestContextHolder.getRequestAttributes().setAttribute("testAttribute", testAttributeValue, SCOPE_REQUEST);
//        RequestContextHolder.getRequestAttributes().setAttribute("orderDto", orderDto, SCOPE_REQUEST);
//
//        testService.testRequestContextHolder();
//
//        HashMap<String, Object> valueMap = new HashMap<>();
//        valueMap.put("orderId", orderDto.getOrderId());
//        valueMap.put("testAttribute", testAttributeValue);
//        String refinedMessage = StringSubstitutor.replace(REQUEST_TEST_MESSAGE, valueMap);
//
//        verify(eventPublisher).publishEvent(recordArgumentCaptor.capture());
//
//        Record record = recordArgumentCaptor.getValue();
//        assertEquals(refinedMessage, record.getMessage());
//        assertEquals(singletonList(orderDto.getOrderId()), record.getIdentifiers().get("orderId"));
//        assertEquals(singletonList(testAttributeValue), record.getIdentifiers().get("testAttribute"));
//        assertNotNull(record.getId());
//    }

    @Test
    public void testInputParamDtoAspectProcessing() {
        ArgumentCaptor<Record> recordArgumentCaptor = ArgumentCaptor.forClass(Record.class);

        ParamDto paramDto = new ParamDto();
        OrderDto orderDto = testService.testInputDtoParam(paramDto);

        verifyInputParam(recordArgumentCaptor, orderDto, paramDto.getWorkerId());
    }

    @Test
    public void testInputParamAspectProcessing() {
        ArgumentCaptor<Record> recordArgumentCaptor = ArgumentCaptor.forClass(Record.class);

        int workerId = new Random().nextInt(10000);
        OrderDto orderDto = testService.testInputParam(workerId);

        verifyInputParam(recordArgumentCaptor, orderDto, workerId);
    }

    private void verifyInputParam(ArgumentCaptor<Record> recordArgumentCaptor,
                                  OrderDto orderDto,
                                  long workerId2) {
        HashMap<String, Object> valueMap = new HashMap<>();
        valueMap.put("orderId", orderDto.getOrderId());
        valueMap.put("skuTitle", orderDto.getSkuTitle());
        valueMap.put("workerId", workerId2);
        String refinedMessage = StringSubstitutor.replace(INPUT_PARAM_TEST_MESSAGE, valueMap);

        verify(eventPublisher).publishEvent(recordArgumentCaptor.capture());

        Record record = recordArgumentCaptor.getValue();
        assertEquals(refinedMessage, record.getMessage());
        assertEquals(singletonList(orderDto.getOrderId()), record.getIdentifiers().get("orderId"));
        assertEquals(singletonList(workerId2), record.getIdentifiers().get("workerId"));
        assertNull(record.getIdentifiers().get("skuTitle"));
        assertNotNull(record.getId());
    }

    @Test
    public void testNestedAspectProcessing() {
        ArgumentCaptor<Record> recordArgumentCaptor = ArgumentCaptor.forClass(Record.class);

        NestedOrderDto orderDto = testService.testNestedOrderDto();

        HashMap<String, Object> valueMap = new HashMap<>();
        valueMap.put("orderId", orderDto.getId());
        valueMap.put("skuTitle", orderDto.getSkuDto().getSkuTitle());
        List<Long> orderItems = orderDto.getOrderItemDto().stream()
                .map(OrderItemDto::getOrderItemId)
                .collect(Collectors.toList());
        valueMap.put("orderItemId", orderItems);

        String refinedMessage = StringSubstitutor.replace(NESTED_ORDER_DTO_TEST_MESSAGE, valueMap);

        verify(eventPublisher).publishEvent(recordArgumentCaptor.capture());

        Record record = recordArgumentCaptor.getValue();
        assertEquals(refinedMessage, record.getMessage());
        assertEquals(singletonList(orderDto.getId()), record.getIdentifiers().get("orderId"));
        assertNull(record.getIdentifiers().get("skuTitle"));
        assertEquals(orderItems, record.getIdentifiers().get("orderItemId"));
        assertNotNull(record.getId());
    }

    @Test
    public void testInheritedAspectProcessing() {
        ArgumentCaptor<Record> recordArgumentCaptor = ArgumentCaptor.forClass(Record.class);

        testService.testInheritedAnnotation(1);

        verify(eventPublisher).publishEvent(recordArgumentCaptor.capture());
    }
}
