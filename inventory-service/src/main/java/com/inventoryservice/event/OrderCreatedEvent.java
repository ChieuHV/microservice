package com.inventoryservice.event;

import lombok.Data;

@Data
public class OrderCreatedEvent {
    private String orderId;
    private String userId;
    private double total;
    private String productId;
    private int quantity;
}
