package com.vitrine.service.impl;

import com.vitrine.api.model.Order;
import com.vitrine.api.model.OrderStatus;
import com.vitrine.api.model.Payment;
import com.vitrine.api.model.PaymentMethod;
import com.vitrine.api.model.PaymentStatus;
import com.vitrine.api.repository.OrderRepository;
import com.vitrine.api.repository.PaymentRepository;
import com.vitrine.api.service.PaymentService;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Optional;

public class PaymentServiceImpl implements PaymentService {
    private final PaymentRepository paymentRepository;
    private final OrderRepository orderRepository;

    public PaymentServiceImpl(PaymentRepository paymentRepository, OrderRepository orderRepository) {
        this.paymentRepository = paymentRepository;
        this.orderRepository = orderRepository;
    }

    @Override
    public Payment process(Long orderId, PaymentMethod method) {
        Order order = orderRepository.findById(orderId)
                .orElseThrow(() -> new IllegalArgumentException("Pedido não encontrado: " +
                        orderId));

        if (order.getStatus() != OrderStatus.PENDING) {
            throw new IllegalStateException("Pedido não está disponível para pagamento.");
        }

        BigDecimal amount = order.getItems().stream()
                .map(item -> item.getUnitPrice().multiply(BigDecimal.valueOf(item.getQuantity())))
                .reduce(BigDecimal.ZERO, BigDecimal::add);

        Payment payment = new Payment();
        payment.setOrder(order);
        payment.setMethod(method);
        payment.setAmount(amount);
        payment.setStatus(PaymentStatus.APPROVED);
        payment.setProcessedAt(LocalDateTime.now());

        paymentRepository.save(payment);

        order.setStatus(OrderStatus.PAID);
        orderRepository.update(order);

        return payment;
    }

    @Override
    public Optional<Payment> findByOrder(Long orderId) {
        return paymentRepository.findByOrderId(orderId);
    }
}
