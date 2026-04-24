package com.paymentservice.service.impl;

import com.paymentservice.event.InventoryReservedEvent;
import com.paymentservice.event.PaymentCompletedEvent;
import com.paymentservice.event.PaymentFailedEvent;
import com.paymentservice.model.Payment;
import com.paymentservice.model.ProcessedEvent;
import com.paymentservice.producer.PaymentEventProducer;
import com.paymentservice.repository.PaymentRepository;
import com.paymentservice.repository.ProcessedEventRepository;
import com.paymentservice.service.OutboxService;
import com.paymentservice.service.PaymentService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class PaymentServiceImpl implements PaymentService {
    private final PaymentRepository paymentRepository;
    private final PaymentEventProducer paymentEventProducer;
    private final ProcessedEventRepository processedEventRepository;
    private final OutboxService outboxService;

    @Override
    public void save(Payment payment) {
        paymentRepository.save(payment);
    }

    @Override
    public List<Payment> findAll() {
        return paymentRepository.findAll();
    }

    @Override
    @Transactional
    public void processPayment(InventoryReservedEvent inventoryReservedEvent) throws Exception {
        String eventId = "PAYMENT" + inventoryReservedEvent.getOrderId();

        if (processedEventRepository.existsById(eventId)) {
            System.out.println("⚠️ Payment đã xử lý rồi: " + eventId);
            return;
        }

        if (paymentRepository.findByOrderId(inventoryReservedEvent.getOrderId()).isPresent()) {
            return;
        }

        try {
            System.out.println("Tiến hành thanh toán");
            Payment payment = new Payment();
            payment.setOrderId(inventoryReservedEvent.getOrderId());
            payment.setAmount(100.0);
            Payment saved = paymentRepository.save(payment);

            PaymentCompletedEvent paymentCompletedEvent = new PaymentCompletedEvent(inventoryReservedEvent.getOrderId(), saved.getId(), saved.getAmount());
//            paymentEventProducer.publishPaymentCompletedEvent(paymentCompletedEvent);
            outboxService.save("payments", inventoryReservedEvent.getOrderId(), paymentCompletedEvent);
        } catch (Exception e) {
            System.out.println("Thanh toán thất bại");
            PaymentFailedEvent paymentFailedEvent = new PaymentFailedEvent(inventoryReservedEvent.getOrderId(), "Insufficient funds");
            outboxService.save("payments_failed", inventoryReservedEvent.getOrderId(), paymentFailedEvent);
            //paymentEventProducer.publishPaymentFailedEvent(paymentFailedEvent);
        }

        processedEventRepository.save(new ProcessedEvent(eventId, "PAYMENT", LocalDateTime.now()));
    }
}
