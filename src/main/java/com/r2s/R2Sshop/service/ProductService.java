package com.r2s.R2Sshop.service;

import com.r2s.R2Sshop.DTO.ProductDTORequest;
import com.r2s.R2Sshop.model.Product;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;


public interface ProductService {
    Product findById(Long id);
    Page<Product> findAllByCategoryIdAndDeleted(Long categoriesId, Integer status, Pageable pageable);
    Product addByCategoryId(Long categoryId, ProductDTORequest dtoRequest);
    Product updateById(Long id, Long categoryId, ProductDTORequest dtoRequest);
    void deleteById(Long id);
    void reactivateById(Long id);
}
