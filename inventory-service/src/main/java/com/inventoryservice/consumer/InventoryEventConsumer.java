package com.inventoryservice.consumer;

import com.inventoryservice.event.InventoryFailedEvent;
import com.inventoryservice.event.InventoryReservedEvent;
import com.inventoryservice.event.OrderCancelledEvent;
import com.inventoryservice.event.OrderCreatedEvent;
import com.inventoryservice.model.Inventory;
import com.inventoryservice.producer.InventoryEventProducer;
import com.inventoryservice.repository.InventoryRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class InventoryEventConsumer {
    private final InventoryEventProducer inventoryEventProducer;

    private final InventoryRepository inventoryRepository;

    @KafkaListener(topics = "orders", groupId = "inventory-service-group", containerFactory = "orderCreatedEventListenerFactory")
    public void consumerOrderCreated(OrderCreatedEvent orderCreatedEvent) {
        System.out.println("Thử lại cập nhập kho hàng");
        Inventory inventory = inventoryRepository.findByProductId(orderCreatedEvent.getProductId()).orElseThrow(() -> new RuntimeException("Không tìm thấy sản phẩm"));

        if (inventory.getQuantity() < orderCreatedEvent.getQuantity()) {
            System.out.println("Hết hàng");
            inventoryEventProducer.publishInventoryFailedEvent(new InventoryFailedEvent(orderCreatedEvent.getOrderId(), "FAILED", "Order failed"));
            return;
        }

        System.out.println("Còn hàng");
        inventory.setQuantity(inventory.getQuantity() - orderCreatedEvent.getQuantity());
        inventoryRepository.save(inventory);
        inventoryEventProducer.publishInventoryReservedEvent(new InventoryReservedEvent(orderCreatedEvent.getOrderId(), "RESERVED", "Reserve successfully"));
    }

    @KafkaListener(topics = "orders_cancelled", groupId = "inventory-cancell-group", containerFactory = "orderCancelledEventListenerFactory")
    public void consumerOrderCancelled(OrderCancelledEvent orderCancelledEvent) {
        Inventory inventory = inventoryRepository.findByProductId(orderCancelledEvent.getProductId()).orElse(null);
        System.out.println("Khôi phục lại kho hàng: " + orderCancelledEvent.getOrderId());
        if (inventory != null) {
            inventory.setQuantity(inventory.getQuantity() + orderCancelledEvent.getQuantity());
            inventoryRepository.save(inventory);
        }
    }
}
