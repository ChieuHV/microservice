package com.paymentservice.event;

import lombok.Data;

@Data
public class InventoryReservedEvent {
    private String orderId;
    private String status;
    private String message;
}
