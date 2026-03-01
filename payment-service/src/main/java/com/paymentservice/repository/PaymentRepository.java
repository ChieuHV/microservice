package com.paymentservice.repository;

import com.paymentservice.model.Payment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.web.bind.annotation.RequestMapping;

@RequestMapping
public interface PaymentRepository extends JpaRepository<Payment, String> {
}
