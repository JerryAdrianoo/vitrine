package com.vitrine.api.repository;

import com.vitrine.api.model.Category;

import java.util.List;
import java.util.Optional;

public interface CategoryRepository {
    Optional<Category> findById(Long id);
    Optional<Category> findByName(String name);
    List<Category> findAll();
    void save(Category category);
    void update(Category category);
    void delete(Long id);
}
