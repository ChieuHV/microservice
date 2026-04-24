package com.orderservice.producer;

import com.orderservice.model.OutboxEvent;
import com.orderservice.repository.OutboxRepository;
import com.orderservice.service.OutboxService;
import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Executor;

@Component
@RequiredArgsConstructor
public class OutboxPublisher {

    private final OutboxRepository outboxRepository;
    private final OutboxService outboxService;
    private final Executor outboxExecutor;

    @Scheduled(fixedDelay = 3000)
    public void publish() {

        List<String> eventIds = outboxService.fetchAndClaimEvents();

        for (String eventId : eventIds) {

            System.out.println("Scheduler thread: "
                    + Thread.currentThread().getName()
                    + " - dispatch eventId: " + eventId);

            outboxExecutor.execute(() -> {
                System.out.println("Worker thread: "
                        + Thread.currentThread().getName()
                        + " - processing eventId: " + eventId);
                processEventSafe(eventId);
            });
        }
    }

    private void processEventSafe(String eventId) {
        try {
            outboxService.processEvent(eventId);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
