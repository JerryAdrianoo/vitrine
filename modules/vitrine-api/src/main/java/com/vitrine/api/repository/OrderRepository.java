package com.vitrine.api.repository;

import com.vitrine.api.model.Order;

import java.util.List;
import java.util.Optional;

public interface OrderRepository {
    Optional<Order> findById(Long id);
    List<Order> findByCustomerId(Long customerId);
    List<Order> findAll();
    void save(Order order);
    void update(Order order);
}
