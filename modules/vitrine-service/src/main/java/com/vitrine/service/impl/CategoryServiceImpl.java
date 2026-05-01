package com.vitrine.service.impl;

import com.vitrine.api.dto.PageResponse;
import com.vitrine.api.model.Category;
import com.vitrine.api.repository.CategoryRepository;
import com.vitrine.api.service.CategoryService;
import java.util.NoSuchElementException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


public class CategoryServiceImpl implements CategoryService {

    private static final Logger logger = LoggerFactory.getLogger(CategoryServiceImpl.class);

    private final CategoryRepository categoryRepository;

    public CategoryServiceImpl(CategoryRepository categoryRepository) {
        this.categoryRepository = categoryRepository;
    }

    @Override
    public Category findById(Long id) {
        logger.info("Finding category by id: {}", id);
        return categoryRepository.findById(id)
                .orElseThrow(() -> new NoSuchElementException("Category not found: " + id));
    }

    @Override
    public PageResponse<Category> findAllPaginated(int page, int size) {
        logger.info("Listing categories - page: {}, size: {}", page, size);

        long total = categoryRepository.countAll();
        var content = categoryRepository.findAllPaginated(page, size);
        return new PageResponse<>(content, page, size, total);
    }

    @Override
    public void create(Category category) {
        logger.info("Creating category: {}", category);
        categoryRepository.findByName(category.getName()).ifPresent(
                c -> {
                    throw new IllegalArgumentException("Category already exists: " + category.getName());
                }
        );

        categoryRepository.save(category);
    }

    @Override
    public void update(Long id, Category category) {
        logger.info("Updating category id: {}", id);
        Category existing = categoryRepository.findById(id)
                .orElseThrow(() -> new NoSuchElementException("Category not found: " + id));

        existing.setName(category.getName());
        categoryRepository.update(existing);
    }

    @Override
    public void delete(Long id) {
        logger.info("Deleting category id: {}", id);
        categoryRepository.findById(id)
                .orElseThrow(() -> new NoSuchElementException("Category not found: " + id));
        categoryRepository.delete(id);
    }
}
