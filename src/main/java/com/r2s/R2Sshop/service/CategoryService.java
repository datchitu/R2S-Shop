package com.r2s.R2Sshop.service;

import com.r2s.R2Sshop.model.Category;

import java.util.List;
import java.util.Optional;

public interface CategoryService {

    List<Category> getAllCategory();

    List<Category> getAllCategoryByDeleted(Boolean deleted);

    Optional<Category> findCategoryById(Long id);

    boolean existsById(Long id);
}
