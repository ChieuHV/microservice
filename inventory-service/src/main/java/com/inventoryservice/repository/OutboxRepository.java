package com.inventoryservice.repository;

import com.inventoryservice.model.OutboxEvent;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface OutboxRepository extends JpaRepository<OutboxEvent, String> {
    List<OutboxEvent> findByStatusAndRetryCountLessThan(String status, int retryCount);
}
