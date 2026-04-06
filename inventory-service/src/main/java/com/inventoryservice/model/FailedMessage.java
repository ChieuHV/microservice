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
public class FailedMessage {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private String id;
    private String orderId;
    @Column(columnDefinition = "TEXT")
    private String payload;
    private String errorMessage;
    private String status; // NEW, RETRIED, RESOLVED
    private LocalDateTime createdAt;
}
