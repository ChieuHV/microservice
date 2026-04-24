package com.inventoryservice.consumer;

import com.inventoryservice.event.InventoryFailedEvent;
import com.inventoryservice.event.InventoryReservedEvent;
import com.inventoryservice.event.OrderCancelledEvent;
import com.inventoryservice.event.OrderCreatedEvent;
import com.inventoryservice.model.Inventory;
import com.inventoryservice.model.ProcessedEvent;
import com.inventoryservice.producer.InventoryEventProducer;
import com.inventoryservice.repository.InventoryRepository;
import com.inventoryservice.repository.ProcessedEventRepository;
import com.inventoryservice.service.OutboxService;
import lombok.RequiredArgsConstructor;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Service
@RequiredArgsConstructor
public class InventoryEventConsumer {
    private final InventoryEventProducer inventoryEventProducer;

    private final InventoryRepository inventoryRepository;
    private final ProcessedEventRepository processedEventRepository;
    private final OutboxService outboxService;
    private final Map<String, Integer> retryCount = new ConcurrentHashMap<>();

    @KafkaListener(topics = "orders", groupId = "inventory-service-group", containerFactory = "orderCreatedEventListenerFactory")
    @Transactional
    public void consumerOrderCreated(OrderCreatedEvent orderCreatedEvent) throws Exception {
        String eventId = "ORDER_CREATED_" + orderCreatedEvent.getOrderId();

        if (processedEventRepository.existsById(eventId)) {
            System.out.println("⚠️ Event đã xử lý rồi, skip: " + eventId);
            return;
        }

//        System.out.println("Cập nhập kho hàng");
//        int count = retryCount.getOrDefault(orderCreatedEvent.getOrderId(), 0);
//
//        System.out.println("Lần xử lý thứ: " + (count + 1));
//
//        if (count == 0) {
//            retryCount.put(orderCreatedEvent.getOrderId(), count + 1);
//            throw new RuntimeException("Giả lập lỗi lần đầu");
//        }
        Inventory inventory = inventoryRepository.findByProductId(orderCreatedEvent.getProductId()).orElseThrow(() -> new RuntimeException("Không tìm thấy sản phẩm"));

        if (inventory.getQuantity() < orderCreatedEvent.getQuantity()) {
            System.out.println("Hết hàng");
            inventoryEventProducer.publishInventoryFailedEvent(new InventoryFailedEvent(orderCreatedEvent.getOrderId(), "FAILED", "Order failed"));
            return;
        }

        System.out.println("Còn hàng");
        inventory.setQuantity(inventory.getQuantity() - orderCreatedEvent.getQuantity());
        inventoryRepository.save(inventory);

        //inventoryEventProducer.publishInventoryReservedEvent(new InventoryReservedEvent(orderCreatedEvent.getOrderId(), "RESERVED", "Reserve successfully"));
        outboxService.save("inventory_reserved", orderCreatedEvent.getOrderId(), new InventoryReservedEvent(orderCreatedEvent.getOrderId(), "RESERVED", "Reserve successfully"));
        processedEventRepository.save(new ProcessedEvent(eventId, "ORDER_CREATED", LocalDateTime.now()));
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
