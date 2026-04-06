package com.inventoryservice.consumer;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.inventoryservice.event.InventoryFailedEvent;
import com.inventoryservice.event.OrderCreatedEvent;
import com.inventoryservice.model.FailedMessage;
import com.inventoryservice.repository.FailedMessageRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;

@Slf4j
@Component
@RequiredArgsConstructor
public class OrderDLQConsumer {

    private final FailedMessageRepository failedMessageRepository;
    private final ObjectMapper objectMapper;
    private final KafkaTemplate<String, Object> kafkaTemplate;
    private static final String TOPIC_INVENTORY_FAILED = "inventory_failed";

    @KafkaListener(topics = "orders.DLT", groupId = "inventory-dlt-group", containerFactory = "orderCreatedEventListenerFactory")
    public void consumeDLQ(OrderCreatedEvent orderCreatedEvent) {
        try {
            System.out.println("orders.DLT: thông báo cập nhập kho hàng không thành công");
            FailedMessage failed = FailedMessage.builder()
                    .orderId(orderCreatedEvent.getOrderId())
                    .payload(objectMapper.writeValueAsString(orderCreatedEvent))
                    .errorMessage("Retry failed after max attempts")
                    .status("NEW")
                    .createdAt(LocalDateTime.now())
                    .build();

            failedMessageRepository.save(failed);

            // 2. Gửi alert (email/slack)
            kafkaTemplate.send(TOPIC_INVENTORY_FAILED, new InventoryFailedEvent(orderCreatedEvent.getOrderId(), "FAILED", "Order failed"));

        } catch (Exception e) {
            log.error("Lỗi khi xử lý DLQ", e);
        }
    }
}
