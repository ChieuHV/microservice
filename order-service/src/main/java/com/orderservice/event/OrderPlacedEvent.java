package com.orderservice.event;

import lombok.*;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Builder
public class OrderPlacedEvent {
    private String orderId;
    private String userId;
    private double total;
};