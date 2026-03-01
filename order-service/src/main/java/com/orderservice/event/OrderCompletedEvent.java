package com.orderservice.event;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.checkerframework.checker.units.qual.N;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class OrderCompletedEvent {
    private String orderId;
    private String userId;
    private String status;
}
