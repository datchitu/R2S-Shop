package com.r2s.R2Sshop.service;

import com.r2s.R2Sshop.DTO.CategoryDTORequest;
import com.r2s.R2Sshop.model.Category;
import java.util.List;
import java.util.Map;
import java.util.Optional;

public interface CategoryService {
    List<Category> getAllByDeleted(Integer status);
    Category findById(Long id);
    Category add(CategoryDTORequest newCategory);
    Category updateById(Long id, CategoryDTORequest DTORequest);
    void deleteById(Long id);
    void reactivateById(Long id);
}
