package com.notificationservice.listener;

import com.notificationservice.event.OrderCancelledEvent;
import com.notificationservice.event.OrderCompletedEvent;
import com.notificationservice.event.OrderPlacedEvent;
import com.notificationservice.service.NotificationService;
import jakarta.mail.MessagingException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

@Component
public class OrderEventListener {
    @Autowired
    private NotificationService notificationService;

    @KafkaListener(topics = "order-topic", groupId = "notification-service-group", containerFactory = "orderPlacedEventListenerFactory")
    public void handleOrderPlaced(OrderPlacedEvent event) throws MessagingException {
        System.out.println("Order received: " + event.getOrderId());
        System.out.println("User: " + event.getUserId());
        System.out.println("Total: " + event.getTotal());

        notificationService.sendMail(
                "havanchieu778@gmail.com",
                "Order " + event.getOrderId(),
                "Total: " + event.getTotal()
        );
    }

    @KafkaListener(topics = "orders_completed", groupId = "notification-service", containerFactory = "orderCompletedEventListenerFactory")
    public void handleOrderCompletedEvent(OrderCompletedEvent event) {
        System.out.println("Order received: " + event.getOrderId());
        System.out.println("User: " + event.getUserId());
        System.out.println("Status: " + event.getStatus());
    }

    @KafkaListener(topics = "orders_cancelled", groupId = "notification-cancelled-group", containerFactory = "orderCancelledEventListenerFactory")
    public void handleOrderCancelledEvent(OrderCancelledEvent event) {
        System.out.println("Order canceled: " + event.getOrderId());
        System.out.println("User: " + event.getUserId());
        System.out.println("Reason: " + event.getReasons());
    }

    @KafkaListener(topics = "inventory_not_enough", groupId = "notification-inventory_not_enough-group", containerFactory = "orderInventoryNotEnoughEventListenerFactory")
    public void handleInventoryNotEnoughEvent(OrderCancelledEvent event) {
        System.out.println("Order canceled: " + event.getOrderId());
        System.out.println("User: " + event.getUserId());
        System.out.println("Reason: " + event.getReasons());
    }
}
