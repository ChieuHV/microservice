package com.paymentservice.producer;

import com.paymentservice.model.OutboxEvent;
import com.paymentservice.repository.OutboxRepository;
import com.paymentservice.service.OutboxService;
import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@RequiredArgsConstructor
public class OutboxPublisher {
    private final OutboxRepository outboxRepository;
    private final OutboxService outboxService;

    @Scheduled(fixedDelay = 3000)
    public void publish() {
        List<OutboxEvent> events = outboxRepository.findByStatusAndRetryCountLessThan("NEW", 3);
        for (OutboxEvent event : events) {
            outboxService.processEvent(event);
        }
    }
}
