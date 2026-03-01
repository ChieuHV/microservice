package com.orderservice.event.consumer;

import lombok.Data;

@Data
public class PaymentCompletedEvent {
    private String orderId;
    private String paymentId;
    private double amount;
}
