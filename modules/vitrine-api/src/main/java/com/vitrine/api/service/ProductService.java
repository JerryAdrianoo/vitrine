package com.vitrine.api.service;

import com.vitrine.api.model.Product;

import java.util.List;
import java.util.Optional;

public interface ProductService {
    void register(Product product, int initialStock);
    Optional<Product> findById(Long id);
    List<Product> findByCategory(Long categoryId);
    List<Product> findAll();
    void update(Product product);
    void delete(Long id);
}
