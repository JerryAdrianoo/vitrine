package com.vitrine.api.service;

import com.vitrine.api.model.Payment;
import com.vitrine.api.model.PaymentMethod;

import java.util.Optional;

public interface PaymentService {
    Payment process(Long orderId, PaymentMethod method);
    Optional<Payment> findByOrder(Long orderId);
}
