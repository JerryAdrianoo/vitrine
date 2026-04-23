package com.vitrine.api.dto;

import com.vitrine.api.model.PaymentMethod;
import com.vitrine.api.model.PaymentStatus;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public class PaymentResponse {

    private Long id;
    private Long orderId;
    private PaymentMethod method;
    private PaymentStatus status;
    private BigDecimal amount;
    private LocalDateTime processedAt;

    public PaymentResponse(Long id, Long orderId, PaymentMethod method,
                           PaymentStatus status, BigDecimal amount, LocalDateTime processedAt) {
        this.id = id;
        this.orderId = orderId;
        this.method = method;
        this.status = status;
        this.amount = amount;
        this.processedAt = processedAt;
    }

    public Long getId() { return id; }
    public Long getOrderId() { return orderId; }
    public PaymentMethod getMethod() { return method; }
    public PaymentStatus getStatus() { return status; }
    public BigDecimal getAmount() { return amount; }
    public LocalDateTime getProcessedAt() { return processedAt; }
}
