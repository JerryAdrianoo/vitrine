package com.vitrine.web.mapper;

import com.vitrine.api.dto.OrderItemResponse;
import com.vitrine.api.dto.OrderResponse;
import com.vitrine.api.model.Order;

import java.util.List;

public class OrderMapperUtil {
    public static OrderResponse toResponse(Order order) {
        List<OrderItemResponse> items = order.getItems().stream()
                .map(item -> new OrderItemResponse(
                        item.getId(),
                        item.getProduct().getName(),
                        item.getQuantity(),
                        item.getUnitPrice()
                ))
                .toList();

        return new OrderResponse(
                order.getId(),
                order.getCustomer().getName(),
                order.getStatus(),
                order.getCreatedAt(),
                items
        );
    }

    private OrderMapperUtil() {}
}
