package com.paymentservice.producer;

import com.paymentservice.event.PaymentCompletedEvent;
import com.paymentservice.event.PaymentFailedEvent;
import lombok.AllArgsConstructor;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class PaymentEventProducer {
    private final KafkaTemplate<String, Object> kafkaTemplate;

    private static final String TOPIC_PAYMENT_COMPLETED = "payments";
    private static final String TOPIC_PAYMENT_FAILED = "payments_failed";

    public void publishPaymentCompletedEvent(PaymentCompletedEvent paymentCompletedEvent) {
        System.out.println("Gửi lại kết quả thanh toán");
        kafkaTemplate.send(TOPIC_PAYMENT_COMPLETED, paymentCompletedEvent);
    }

    public void publishPaymentFailedEvent(PaymentFailedEvent paymentFailedEvent) {
        System.out.println("Gửi lại kết quả thanh toán");
        kafkaTemplate.send(TOPIC_PAYMENT_FAILED, paymentFailedEvent);
    }
}
