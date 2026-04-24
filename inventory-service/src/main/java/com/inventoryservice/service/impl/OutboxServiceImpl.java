package com.inventoryservice.service.impl;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.inventoryservice.event.InventoryReservedEvent;
import com.inventoryservice.model.OutboxEvent;
import com.inventoryservice.repository.OutboxRepository;
import com.inventoryservice.service.OutboxService;
import lombok.AllArgsConstructor;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;

@Service
@AllArgsConstructor
public class OutboxServiceImpl implements OutboxService {

    private final OutboxRepository outboxRepository;
    private final ObjectMapper objectMapper;
    private final KafkaTemplate<String, Object> kafkaTemplate;

    @Override
    public void save(String topic, String orderId, Object event) throws Exception {
        OutboxEvent outbox = OutboxEvent.builder()
                .topic(topic)
                .aggregateId(orderId)
                .payload(objectMapper.writeValueAsString(event))
                .status("NEW")
                .retryCount(0)
                .createdAt(LocalDateTime.now())
                .build();

        outboxRepository.save(outbox);
    }

    @Override
    @Transactional
    public void processEvent(OutboxEvent event) {
        try {
            System.out.println("Outbox events chạy");

            System.out.println("Gửi event");
            kafkaTemplate.send(event.getTopic(), event.getAggregateId(), objectMapper.readValue(event.getPayload(), InventoryReservedEvent.class)).get();
            event.setStatus("SENT");
        } catch (Exception e) {
            System.err.println("Error occurred while sending events");

            event.setRetryCount(event.getRetryCount() + 1);
            event.setErrorMessage(e.getMessage());

            if (event.getRetryCount() >= 3) {
                event.setStatus("FAILED");
            }
        }

        outboxRepository.save(event);
    }
}
