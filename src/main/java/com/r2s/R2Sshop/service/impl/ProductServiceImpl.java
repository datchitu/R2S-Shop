package com.r2s.R2Sshop.service.impl;

import com.r2s.R2Sshop.model.Product;
import com.r2s.R2Sshop.repository.ProductRepository;
import com.r2s.R2Sshop.service.ProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import java.util.Optional;

@Service
public class ProductServiceImpl implements ProductService {
    @Autowired
    private ProductRepository productRepository;

    /**
     * Return product by id.
     * <p>
     * This function returns product by id, with the id as the input parameter
     * @param id
     * @return Product by id
     * @author HoangVu
     * @since 1.0
     */
    @Override
    public Optional<Product> findById(Long id) {
        return this.productRepository.findById(id);
    }

    /**
     * Return all products by category id.
     * <p>
     * This function returns all product by categoryId, with the categoryId as the input parameter
     * and pagination is applied.
     * @param categoryId
     * @param pageable
     * @return All product by categoryId and pagination is applied
     * @author HoangVu
     * @since 1.0
     */
    @Override
    public Page<Product> findAllByCategoryId(Long categoryId, Pageable pageable) {
        return this.productRepository.findAllByCategory_Id(categoryId, pageable);
    }

    /**
     * Check exists product by id.
     * <p>
     * This function check exists product by id, with the id as the input parameter.
     * @param id
     * @return True if it exists and an error if it does not.
     * @author HoangVu
     * @since 1.0
     */
    @Override
    public Boolean existsById(Long id) {
        return productRepository.existsById(id);
    }
}
