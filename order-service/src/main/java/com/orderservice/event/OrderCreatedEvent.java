package com.orderservice.event;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class OrderCreatedEvent {
    private String orderId;
    private String userId;
    private double total;
    private String productId;
    private int quantity;
}
