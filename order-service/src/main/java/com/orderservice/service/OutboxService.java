package com.orderservice.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.orderservice.model.OutboxEvent;

import java.util.List;

public interface OutboxService {
    void save(String topic, String orderId, Object event) throws Exception;

    List<String> fetchAndClaimEvents();

    void processEvent(String eventId);
}
