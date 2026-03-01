package com.inventoryservice.producer;

import com.inventoryservice.event.InventoryFailedEvent;
import com.inventoryservice.event.InventoryReservedEvent;
import lombok.AllArgsConstructor;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class InventoryEventProducer {
    private final KafkaTemplate<String, Object> kafkaTemplate;

    private static final String TOPIC_INVENTORY_RESERVED= "inventory_reserved";
    private static final String TOPIC_INVENTORY_FAILED = "inventory_failed";

    public void publishInventoryReservedEvent(InventoryReservedEvent inventoryReservedEvent) {
        System.out.println("Gửi yêu cầu thanh toán");
        kafkaTemplate.send(TOPIC_INVENTORY_RESERVED, inventoryReservedEvent);
    }

    public void publishInventoryFailedEvent(InventoryFailedEvent inventoryFailedEvent) {
        kafkaTemplate.send(TOPIC_INVENTORY_FAILED, inventoryFailedEvent);
    }
}
