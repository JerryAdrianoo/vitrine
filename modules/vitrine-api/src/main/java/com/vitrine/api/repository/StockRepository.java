package com.vitrine.api.repository;

import com.vitrine.api.model.Stock;

import java.util.Optional;

public interface StockRepository {
    Optional<Stock> findById(Long id);
    Optional<Stock> findByProductId(Long productId);
    void save(Stock stock);
    void update(Stock stock);
}
