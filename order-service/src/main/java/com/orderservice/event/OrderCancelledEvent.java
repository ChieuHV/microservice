package com.orderservice.event;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class OrderCancelledEvent {
    private String orderId;
    private String userId;
    private String productId;
    private int quantity;
    private String reasons;
}
