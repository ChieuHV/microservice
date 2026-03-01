package com.orderservice.service.impl;

import com.orderservice.dto.UserDTO;
import com.orderservice.event.OrderCancelledEvent;
import com.orderservice.event.OrderCompletedEvent;
import com.orderservice.event.OrderCreatedEvent;
import com.orderservice.event.OrderPlacedEvent;
import com.orderservice.model.Order;
import com.orderservice.model.OrderStatus;
import com.orderservice.openfeignclient.UserClient;
import com.orderservice.producer.OrderEventProducer;
import com.orderservice.repository.OrderServiceRepository;
import com.orderservice.service.OrderService;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationToken;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@AllArgsConstructor
public class OrderServiceImpl implements OrderService {

    private final OrderServiceRepository orderServiceRepository;
    private final KafkaTemplate<String, OrderPlacedEvent> kafkaTemplate;
    private final UserClient userClient;
    private final OrderEventProducer orderEventProducer;

//    @Override
//    public void save(Order order) {
//        order.setUserId((String) SecurityContextHolder.getContext().getAuthentication().getPrincipal());
//        Order orderSaved = orderServiceRepository.save(order);
//
//        OrderPlacedEvent event = OrderPlacedEvent.builder()
//                .orderId(orderSaved.getId())
//                .userId(orderSaved.getUserId())
//                .total(orderSaved.getPrice())
//                .build();
//
//        kafkaTemplate.send("order-topic", orderSaved.getId(), event);
//    }

//    @Override
//    public void save(Order order, JwtAuthenticationToken jwtAuthenticationToken) {
//        String sub = jwtAuthenticationToken.getToken().getSubject();
//        UserDTO user = userClient.getUserByKeycloakId(sub);
//
//        order.setUserId(user.getId());
//        Order orderSaved = orderServiceRepository.save(order);
//
//        OrderPlacedEvent event = OrderPlacedEvent.builder()
//                .orderId(orderSaved.getId())
//                .userId(orderSaved.getUserId())
//                .total(orderSaved.getPrice())
//                .build();
//
//        kafkaTemplate.send("order-topic", orderSaved.getId(), event);
//    }

    @Override
    public void save(Order order, JwtAuthenticationToken jwtAuthenticationToken) {
        String sub = jwtAuthenticationToken.getToken().getSubject();
        UserDTO user = userClient.getUserByKeycloakId(sub);

        order.setUserId(user.getId());
        order.setTotal(Double.parseDouble(String.valueOf(order.getQuantity() * order.getPrice())));
        order.setOrderStatus(OrderStatus.PENDING);
        Order saved = orderServiceRepository.save(order);

        OrderCreatedEvent orderCreatedEvent = OrderCreatedEvent.builder()
                .orderId(saved.getId())
                .userId(saved.getUserId())
                .productId(saved.getProductId())
                .quantity(saved.getQuantity())
                .total(saved.getTotal())
                .build();
        System.out.println("Tạo đơn hàng");
        orderEventProducer.publishOrderCreatedEvent(orderCreatedEvent);
    }

    @Override
    public List<Order> findAll() {
        return orderServiceRepository.findAll();
    }

    @Override
    public Order findById(String id) {
        return orderServiceRepository.findById(id).map(order -> new Order(
                order.getId()
                , order.getProductName()
                , order.getPrice()
                , order.getUserId()
                , order.getProductId()
                , order.getQuantity()
                , order.getTotal()
                , order.getOrderStatus()
        )).orElse(new Order());
    }

    @Override
    public void updateOrderStatus(String orderId, OrderStatus orderStatus) {
        orderServiceRepository.findById(orderId).ifPresent(order -> {
            order.setOrderStatus(orderStatus);
            Order updated = orderServiceRepository.save(order);
            System.out.println("Cập nhập trạng thái đơn hàng");
            if (orderStatus.equals(OrderStatus.COMPLETED)) {
                OrderCompletedEvent event = new OrderCompletedEvent(
                        updated.getId(),
                        updated.getUserId(),
                        orderStatus.name()
                );

                orderEventProducer.publishOrderCompletedEvent(event);
            } else if (orderStatus.equals(OrderStatus.CANCELLED)) {
                OrderCancelledEvent orderCancelledEvent = new OrderCancelledEvent(
                        updated.getId(),
                        updated.getUserId(),
                        updated.getProductId(),
                        updated.getQuantity(),
                        "Order cancelled (payment failed)"
                );

                orderEventProducer.publishOrderCancelledEvent(orderCancelledEvent);
            } else if (orderStatus.equals(OrderStatus.INVENTORY_FAILED)) {
                OrderCancelledEvent orderCancelledEvent = new OrderCancelledEvent(
                        updated.getId(),
                        updated.getUserId(),
                        updated.getProductId(),
                        updated.getQuantity(),
                        "Inventory is not enough"
                );

                orderEventProducer.publishOrderInventoryNoEnoughEvent(orderCancelledEvent);
            }
        });
    }
}
