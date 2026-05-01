package com.vitrine.api.service;

import com.vitrine.api.dto.PageResponse;
import com.vitrine.api.model.Order;
import com.vitrine.api.model.OrderItem;

import java.util.List;
import java.util.Optional;

public interface OrderService {
   Order placeOrder(Long customerId, List<OrderItem> items);
   Optional<Order> findById(Long id);
   List<Order> findByCustomer(Long customerId);
   PageResponse<Order> findAllPaginated(int page, int size);
   void cancel(Long orderId);
}
