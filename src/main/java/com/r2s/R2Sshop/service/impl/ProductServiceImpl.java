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
     * @return Information of Product by id
     * @author HoangVu
     * @since 1.0
     */
    @Override
    public Optional<Product> findById(Long id) {
        return this.productRepository.findById(id);
    }

    /**
     * Return all products by category id and deleted.
     * <p>
     * This function returns all product by categoryId and deleted,
     * with the categoryId, deleted, pageable as the input parameter
     * and pagination is applied.
     * @param categoryId
     * @param deleted
     * @param pageable
     * @return Product list by categoryId and pagination is applied
     * @author HoangVu
     * @since 1.1
     */
    @Override
    public Page<Product> findAllByCategoryIdAndDeleted(Long categoryId, Boolean deleted, Pageable pageable) {
        return this.productRepository.findAllByCategory_IdAndDeleted(categoryId, deleted, pageable);
    }

    /**
     * Check exists product by id.
     * <p>
     * This function check exists product by id, with the id as the input parameter.
     * @param id
     * @return True if it exists and an false if it does not.
     * @author HoangVu
     * @since 1.0
     */
    @Override
    public Boolean existsById(Long id) {
        return productRepository.existsById(id);
    }
}
