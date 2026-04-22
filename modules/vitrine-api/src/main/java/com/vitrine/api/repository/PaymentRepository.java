package com.vitrine.api.repository;

import com.vitrine.api.model.Payment;

import java.util.Optional;

public interface PaymentRepository {
    Optional<Payment> findById(Long id);
    Optional<Payment> findByOrderId(Long orderId);
    void save(Payment payment);
    void update(Payment payment);
}
