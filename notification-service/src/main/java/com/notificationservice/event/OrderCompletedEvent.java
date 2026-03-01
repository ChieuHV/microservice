package com.notificationservice.event;

import lombok.Data;

@Data
public class OrderCompletedEvent {
    private String orderId;
    private String userId;
    private String status;
}
