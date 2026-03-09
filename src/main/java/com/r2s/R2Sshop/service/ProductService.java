package com.r2s.R2Sshop.service;

import com.r2s.R2Sshop.model.Category;
import com.r2s.R2Sshop.model.Product;
import com.r2s.R2Sshop.repository.ProductRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class ProductService {
    @Autowired
    private ProductRepository productRepository;

    private CategoryService categoryService;

    public Product findProductById(long id){
        return this.productRepository.findById(id).orElse(null);
    }

//    public Product findAllProductsByCategory(long id){
//
//        return this.productRepository.findById(id).orElse(null);
//    }

}
