package com.orderservice.service.impl;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.orderservice.event.OrderCreatedEvent;
import com.orderservice.model.OutboxEvent;
import com.orderservice.repository.OutboxRepository;
import com.orderservice.service.OutboxService;
import lombok.AllArgsConstructor;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

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

    @Transactional
    public List<String> fetchAndClaimEvents() {

        List<OutboxEvent> events = outboxRepository.findBatchForUpdate();

        List<String> eventIds = new ArrayList<>();

        for (OutboxEvent event : events) {
            event.setStatus("PROCESSING");
            eventIds.add(event.getId());
        }

        // save để persist trạng thái
        outboxRepository.saveAll(events);

        return eventIds;
    }

    @Override
    @Transactional
    public void processEvent(String eventId) {

        OutboxEvent event = outboxRepository.findById(eventId)
                .orElseThrow(() -> new RuntimeException("Event not found"));

        if (!"PROCESSING".equals(event.getStatus())) {
            return;
        }

        try {
            outboxRepository.save(event);

            kafkaTemplate.send(
                    event.getTopic(),
                    event.getAggregateId(),
                    objectMapper.readValue(event.getPayload(), OrderCreatedEvent.class)
            ).get(); // đảm bảo ACK

            event.setStatus("SENT");

        } catch (Exception e) {

            event.setRetryCount(event.getRetryCount() + 1);
            event.setErrorMessage(e.getMessage());

            if (event.getRetryCount() >= 3) {
                event.setStatus("FAILED");
            } else {
                event.setStatus("NEW");
            }
        }

        outboxRepository.save(event);
    }
}
