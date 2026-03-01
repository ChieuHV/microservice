package com.orderservice.event.consumer;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class InventoryFailedEvent {
    private String orderId;
    private String status;
    private String message;
}
