package com.paymentservice.service;

import com.paymentservice.event.InventoryReservedEvent;
import com.paymentservice.model.Payment;

import java.util.List;

public interface PaymentService {
    void save(Payment payment);

    List<Payment> findAll();

    void processPayment(InventoryReservedEvent inventoryReservedEvent) throws Exception;
}
