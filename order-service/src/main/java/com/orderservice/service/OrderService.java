package com.orderservice.service;

import com.orderservice.model.Order;

import java.util.List;

public interface OrderService {
    void save(Order order);

    List<Order> findAll();

    Order findById(String id);
}
