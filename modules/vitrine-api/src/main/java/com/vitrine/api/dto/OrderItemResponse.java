package com.vitrine.api.dto;

import java.math.BigDecimal;

public class OrderItemResponse {
    private Long id;
    private String productName;
    private Integer quantity;

    public BigDecimal getSubtotal() {
        return subtotal;
    }

    public BigDecimal getUnitPrice() {
        return unitPrice;
    }

    public Integer getQuantity() {
        return quantity;
    }

    public String getProductName() {
        return productName;
    }

    public Long getId() {
        return id;
    }

    private BigDecimal unitPrice;
    private BigDecimal subtotal;

    public OrderItemResponse(Long id, String productName, Integer quantity, BigDecimal unitPrice) {
        this.id = id;
        this.productName = productName;
        this.quantity = quantity;
        this.unitPrice = unitPrice;
        this.subtotal = unitPrice.multiply(BigDecimal.valueOf(quantity));
    }
}
