package com.vitrine.service.impl;

import com.vitrine.api.dto.PageResponse;
import com.vitrine.api.model.Product;
import com.vitrine.api.model.Stock;
import com.vitrine.api.repository.ProductRepository;
import com.vitrine.api.repository.StockRepository;
import com.vitrine.api.service.ProductService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;
import java.util.Optional;

public class ProductServiceImpl implements ProductService {

    private static final Logger logger = LoggerFactory.getLogger(ProductServiceImpl.class);

    private final ProductRepository productRepository;
    private final StockRepository stockRepository;

    public ProductServiceImpl(ProductRepository productRepository, StockRepository stockRepository) {
        this.productRepository = productRepository;
        this.stockRepository = stockRepository;
    }

    @Override
    public void register(Product product, int initialStock) {
        if (initialStock < 0) {
            logger.warn("Product registration rejected — negative initial stock: {}", initialStock);
            throw new IllegalArgumentException("Initial stock can not be zero.");
        }

        productRepository.save(product);

        Stock stock = new Stock();
        stock.setProduct(product);
        stock.setQuantity(initialStock);

        stockRepository.save(stock);
        logger.info("Product registered: {} with initial stock {}", product.getName(), initialStock);
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
    public PageResponse<Product> findAllPaginated(int page, int size) {
        List<Product> items = productRepository.findAllPaginated(page, size);
        long total = productRepository.countAll();
        return new PageResponse<>(items, page, size, total);
    }

    @Override
    public void update(Product product) {
        productRepository.update(product);
    }

    @Override
    public void delete(Long id) {
        productRepository.delete(id);
        logger.info("Product deleted: {}", id);
    }
}
