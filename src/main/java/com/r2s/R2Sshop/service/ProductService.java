package com.r2s.R2Sshop.service;

import com.r2s.R2Sshop.model.Product;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import java.util.Optional;


public interface ProductService {
    Optional<Product> findById(Long id);

    Page<Product> findAllByCategoryId(Long categoriesId, Pageable pageable);

    Boolean existsById(Long id);
}
