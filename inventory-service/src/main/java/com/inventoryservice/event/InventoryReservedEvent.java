package com.inventoryservice.event;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class InventoryReservedEvent {
    private String orderId;
    private String status;
    private String message;
}
