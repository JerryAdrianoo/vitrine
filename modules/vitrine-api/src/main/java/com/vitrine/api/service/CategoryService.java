package com.vitrine.api.service;

import com.vitrine.api.dto.PageResponse;
import com.vitrine.api.model.Category;

public interface CategoryService {
    Category findById(Long id);
    PageResponse<Category> findAllPaginated(int page, int size);
    void create (Category category);
    void update (Long id, Category category);
    void delete (Long id);
}
