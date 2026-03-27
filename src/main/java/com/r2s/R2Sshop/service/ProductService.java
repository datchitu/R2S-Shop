package com.r2s.R2Sshop.service;

import com.r2s.R2Sshop.model.Category;
import com.r2s.R2Sshop.model.Product;
import com.r2s.R2Sshop.repository.ProductRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;


public interface ProductService {

    Product findProductById(Long id);

    List<Product> findAllProductByCategoryId(Long categoriesId);
}
