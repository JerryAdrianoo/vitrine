package com.vitrine.api.dto;

import com.vitrine.api.model.OrderStatus;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

public class OrderResponse {

    private Long id;
    private String customerName;
    private OrderStatus status;
    private LocalDateTime createdAt;
    private List<OrderItemResponse> items;
    private BigDecimal total;

    public OrderResponse(Long id, String customerName, OrderStatus status,
                         LocalDateTime createdAt, List<OrderItemResponse> items) {
        this.id = id;
        this.customerName = customerName;
        this.status = status;
        this.createdAt = createdAt;
        this.items = items;
        this.total = items.stream()
                .map(OrderItemResponse::getSubtotal)
                .reduce(BigDecimal.ZERO, BigDecimal::add);
    }

    public Long getId() {
        return id;
    }

    public String getCustomerName() {
        return customerName;
    }

    public OrderStatus getStatus() {
        return status;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public List<OrderItemResponse> getItems() {
        return items;
    }

    public BigDecimal getTotal() {
        return total;
    }
}