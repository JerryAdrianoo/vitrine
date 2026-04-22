package com.vitrine.service.impl;

import com.vitrine.api.model.Product;
import com.vitrine.api.model.Stock;
import com.vitrine.api.repository.ProductRepository;
import com.vitrine.api.repository.StockRepository;
import com.vitrine.api.service.ProductService;

import java.util.List;
import java.util.Optional;

public class ProductServiceImpl implements ProductService {

    private final ProductRepository productRepository;
    private final StockRepository stockRepository;

    public ProductServiceImpl(ProductRepository productRepository, StockRepository
            stockRepository) {
        this.productRepository = productRepository;
        this.stockRepository = stockRepository;
    }

    @Override
    public void register(Product product, int initialStock) {
        if (initialStock < 0) {
            throw new IllegalArgumentException("Initial stock can not be zero.");
        }

        productRepository.save(product);

        Stock stock = new Stock();
        stock.setProduct(product);
        stock.setQuantity(initialStock);

        stockRepository.save(stock);
    }

    @Override
    public Optional<Product> findById(Long id) {
        return productRepository.findById(id);
    }

    @Override
    public List<Product> findByCategory(Long categoryId) {
        return productRepository.findByCategory(categoryId);
    }

    @Override
    public List<Product> findAll() {
        return productRepository.findAll();
    }

    @Override
    public void update(Product product) {
        productRepository.update(product);
    }

    @Override
    public void delete(Long id) {
        productRepository.delete(id);
    }
}
