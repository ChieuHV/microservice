package com.orderservice.consumer;

import com.orderservice.event.consumer.InventoryFailedEvent;
import com.orderservice.event.consumer.PaymentCompletedEvent;
import com.orderservice.event.consumer.PaymentFailedEvent;
import com.orderservice.model.OrderStatus;
import com.orderservice.service.OrderService;
import lombok.RequiredArgsConstructor;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class OrderEventConsumer {

    private final OrderService orderService;

    @KafkaListener(topics = "inventory_failed", containerFactory = "inventoryFailedKafkaListenerContainerFactory")
    public void handleInventoryFailed(InventoryFailedEvent event) {
        orderService.updateOrderStatus(event.getOrderId(), OrderStatus.INVENTORY_FAILED);
    }

    @KafkaListener(topics = "payments", containerFactory = "paymentCompletedKafkaListenerContainerFactory")
    public void handlePaymentCompleted(PaymentCompletedEvent event) {
        System.out.println("Nhận kết quả thanh toán");
        orderService.updateOrderStatus(event.getOrderId(), OrderStatus.COMPLETED);
    }

    @KafkaListener(topics = "payments_failed", containerFactory = "paymentFailedKafkaListenerContainerFactory")
    public void handlePaymentFailed(PaymentFailedEvent event) {
        orderService.updateOrderStatus(event.getOrderId(), OrderStatus.CANCELLED);
    }
}

