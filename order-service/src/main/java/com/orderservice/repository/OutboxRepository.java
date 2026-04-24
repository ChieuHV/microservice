package com.orderservice.repository;

import com.orderservice.model.OutboxEvent;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface OutboxRepository extends JpaRepository<OutboxEvent, String> {
    @Query(value = """
    SELECT * FROM outbox_event
    WHERE status = 'NEW'
    AND retry_count < 3
    ORDER BY created_at
    LIMIT 50
    FOR UPDATE SKIP LOCKED
""", nativeQuery = true)
    List<OutboxEvent> findBatchForUpdate();
}
