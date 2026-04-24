package com.inventoryservice.service;

import com.inventoryservice.model.OutboxEvent;

public interface OutboxService {
    void save(String topic, String orderId, Object event) throws Exception;

    void processEvent(OutboxEvent event);
}
