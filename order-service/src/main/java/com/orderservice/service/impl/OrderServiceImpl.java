package com.orderservice.service.impl;

import com.orderservice.model.Order;
import com.orderservice.repository.OrderServiceRepository;
import com.orderservice.service.OrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class OrderServiceImpl implements OrderService {

    @Autowired
    private OrderServiceRepository orderServiceRepository;

    @Override
    public void save(Order order) {
        orderServiceRepository.save(order);
    }

    @Override
    public List<Order> findAll() {
        return orderServiceRepository.findAll();
    }

    @Override
    public Order findById(String id) {
        return orderServiceRepository.findById(id).map(order -> new Order(order.getId(), order.getProductName(), order.getPrice(), order.getUserId())).orElse(new Order());
    }
}
