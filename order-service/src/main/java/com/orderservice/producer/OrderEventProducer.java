package com.orderservice.producer;

import com.orderservice.event.OrderCancelledEvent;
import com.orderservice.event.OrderCompletedEvent;
import com.orderservice.event.OrderCreatedEvent;
import lombok.RequiredArgsConstructor;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class OrderEventProducer {

    private final KafkaTemplate<String, Object> kafkaTemplate;

    private static final String TOPIC_ORDER_CREATED = "orders";
    private static final String TOPIC_ORDER_COMPLETED = "orders_completed";
    private static final String TOPIC_ORDER_CANCELL = "orders_cancelled";
    private static final String TOPIC_INVENTORY_NOT_ENOUGH = "inventory_not_enough";

    public void publishOrderCreatedEvent(OrderCreatedEvent event) {
        System.out.println("Gửi đơn hàng");
        kafkaTemplate.send(TOPIC_ORDER_CREATED, String.valueOf(event.getOrderId()), event);
    }

    public void publishOrderCompletedEvent(OrderCompletedEvent event) {
        System.out.println("Gửi thông báo đơn hàng");
        kafkaTemplate.send(TOPIC_ORDER_COMPLETED, String.valueOf(event.getOrderId()), event);
    }

    public void publishOrderCancelledEvent(OrderCancelledEvent event) {
        System.out.println("Hủy đơn hàng");
        kafkaTemplate.send(TOPIC_ORDER_CANCELL, String.valueOf(event.getOrderId()), event);
    }

    public void publishOrderInventoryNoEnoughEvent(OrderCancelledEvent event) {
        System.out.println("Không đủ hàng");
        kafkaTemplate.send( TOPIC_INVENTORY_NOT_ENOUGH, String.valueOf(event.getOrderId()), event);
    }
}

