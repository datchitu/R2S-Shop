package com.r2s.R2Sshop.service;

import com.r2s.R2Sshop.DTO.CategoryDTORequest;
import com.r2s.R2Sshop.model.Category;
import java.util.List;
import java.util.Map;
import java.util.Optional;

public interface CategoryService {
    List<Category> getAllByDeleted(Boolean deleted);
    Optional<Category> findById(Long id);
    Boolean existsById(Long id);
    Boolean existsByName(String name);
    Category add(CategoryDTORequest newCategory);
    Category updateById(Long id, CategoryDTORequest DTORequest);
    Category deleteById(Long id);
    Category reactivateById(Long id);
}
