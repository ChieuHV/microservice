package com.inventoryservice.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
public class OutboxEvent {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private String id;
    private String topic;
    private String aggregateId; // orderId
    @Column(columnDefinition = "TEXT")
    private String payload;
    private String status; // NEW, SENT, FAILED
    private Integer retryCount;
    private String errorMessage;
    private LocalDateTime createdAt;
}
