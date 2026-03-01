package com.paymentservice.service.impl;

import com.paymentservice.event.InventoryReservedEvent;
import com.paymentservice.event.PaymentCompletedEvent;
import com.paymentservice.event.PaymentFailedEvent;
import com.paymentservice.model.Payment;
import com.paymentservice.producer.PaymentEventProducer;
import com.paymentservice.repository.PaymentRepository;
import com.paymentservice.service.PaymentService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class PaymentServiceImpl implements PaymentService {
    private final PaymentRepository paymentRepository;
    private final PaymentEventProducer paymentEventProducer;

    @Override
    public void save(Payment payment) {
        paymentRepository.save(payment);
    }

    @Override
    public List<Payment> findAll() {
        return paymentRepository.findAll();
    }

    @Override
    public void processPayment(InventoryReservedEvent inventoryReservedEvent) {
        try {
            System.out.println("Tiến hành thanh toán");
            Payment payment = new Payment();
            payment.setOrderId(inventoryReservedEvent.getOrderId());
            payment.setAmount(100.0);
            Payment saved = paymentRepository.save(payment);

            PaymentCompletedEvent paymentCompletedEvent = new PaymentCompletedEvent(inventoryReservedEvent.getOrderId(), saved.getId(), saved.getAmount());
            paymentEventProducer.publishPaymentCompletedEvent(paymentCompletedEvent);
        } catch (Exception e) {
            System.out.println("Thanh toán thất bại");
            PaymentFailedEvent paymentFailedEvent = new PaymentFailedEvent(inventoryReservedEvent.getOrderId(), "Insufficient funds");
            paymentEventProducer.publishPaymentFailedEvent(paymentFailedEvent);
        }
    }
}
