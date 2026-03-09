package com.r2s.R2Sshop.service;

import com.r2s.R2Sshop.model.Category;
import com.r2s.R2Sshop.repository.CategoryRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CategoryService {
    @Autowired
    private CategoryRepository categoryRepository;

    public List<Category> getAllCategory() {
        return this.categoryRepository.findAll();
    }

    public Category findCategoryById(long id){
        return this.categoryRepository.findById(id).orElse(null);
    }
}
