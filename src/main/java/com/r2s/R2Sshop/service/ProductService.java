package com.r2s.R2Sshop.service;

import com.r2s.R2Sshop.model.Category;
import com.r2s.R2Sshop.model.Product;
import com.r2s.R2Sshop.repository.ProductRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;


public interface ProductService {

    Product findProductById(Long id);

    Page<Product> findAllProductsByCategoryId(Long categoriesId, Pageable pageable);
}
