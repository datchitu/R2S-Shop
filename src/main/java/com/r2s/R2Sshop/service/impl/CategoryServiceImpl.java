package com.r2s.R2Sshop.service.impl;

import com.r2s.R2Sshop.constants.ResponseCode;
import com.r2s.R2Sshop.model.Category;
import com.r2s.R2Sshop.repository.CategoryRepository;
import com.r2s.R2Sshop.rest.AppException;
import com.r2s.R2Sshop.service.CategoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CategoryServiceImpl implements CategoryService {
    @Autowired
    private CategoryRepository categoryRepository;

    /**
     * Return category list.
     * <p>
     * This function returns all category.
     * @return Category list
     * @author HoangVu
     * @since 1.0
     */
    @Override
    public List<Category> getAllCategory() {
        return this.categoryRepository.findAll();
    }

    /**
     * Return category list by deleted.
     * <p>
     * This function returns all category by deleted, with the deleted as the input parameter.
     * @param deleted
     * @return Category list by deleted
     * @author HoangVu
     * @since 1.0
     */
    @Override
    public List<Category> getAllCategoryByDeleted(Boolean deleted) {
        return this.categoryRepository.findAllByDeleted(deleted);
    }

    /**
     * Return category by id.
     * <p>
     * This function returns category by id, with the id as the input parameter.
     * @param id
     * @return Category by id
     * @author HoangVu
     * @since 1.0
     */
    @Override
    public Category findCategoryById(Long id){
        return this.categoryRepository.findById(id).orElse(null);
    }

    /**
     * Check exists category by id.
     * <p>
     * This function check exists category by id, with the id as the input parameter.
     * @param id
     * @return True if it exists and an error if it does not.
     * @author HoangVu
     * @since 1.1
     */
    @Override
    public boolean existsById(Long id) {
        return categoryRepository.existsById(id);
    }
}
