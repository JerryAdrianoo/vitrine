package com.vitrine.api.repository;

import com.vitrine.api.model.Product;

import java.util.List;
import java.util.Optional;

public interface ProductRepository {
    Optional<Product> findById(Long id);
    List<Product> findByCategory(Long categoryId);
    List<Product> findAll();
    List<Product> findAllPaginated(int page, int size);
    long countAll();
    void save(Product product);
    void update(Product product);
    void delete(Long id);
}
