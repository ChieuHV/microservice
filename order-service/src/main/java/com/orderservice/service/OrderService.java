package com.orderservice.service;

import com.orderservice.model.Order;
import com.orderservice.model.OrderStatus;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationToken;

import java.util.List;

public interface OrderService {
//    void save(Order order, JwtAuthenticationToken jwtAuthenticationToken);

    void save(Order order, JwtAuthenticationToken jwtAuthenticationToken) throws Exception;

    List<Order> findAll();

    Order findById(String id);

    void updateOrderStatus(String orderId, OrderStatus orderStatus);
}
