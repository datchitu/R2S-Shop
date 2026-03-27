package com.r2s.R2Sshop.service.impl;

import com.r2s.R2Sshop.model.Product;
import com.r2s.R2Sshop.repository.ProductRepository;
import com.r2s.R2Sshop.service.ProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ProductServiceImpl implements ProductService {
    @Autowired
    private ProductRepository productRepository;

    @Override
    public Product findProductById(Long id) {
        return this.productRepository.findById(id).orElse(null);
    }
    @Override
    public List<Product> findAllProductByCategoryId(Long categoryId) {
        return this.productRepository.findByCategory_Id(categoryId);
    }
}
