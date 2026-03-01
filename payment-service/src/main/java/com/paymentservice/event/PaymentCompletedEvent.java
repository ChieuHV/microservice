package com.paymentservice.event;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class PaymentCompletedEvent {
    private String orderId;
    private String paymentId;
    private double amount;
}
