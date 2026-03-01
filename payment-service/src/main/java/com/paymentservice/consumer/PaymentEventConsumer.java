package com.paymentservice.consumer;

import com.paymentservice.event.InventoryReservedEvent;
import com.paymentservice.service.PaymentService;
import lombok.RequiredArgsConstructor;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class PaymentEventConsumer {

    private final PaymentService paymentService;

    @KafkaListener(topics = "inventory_reserved", groupId = "payment-service-group", containerFactory = "orderInventoryReservedEventListenerFactory")
    public void consumerInventoryReserved(InventoryReservedEvent inventoryReservedEvent) {
        System.out.println("Nhận yêu cầu thanh toán");
        paymentService.processPayment(inventoryReservedEvent);
    }
}
