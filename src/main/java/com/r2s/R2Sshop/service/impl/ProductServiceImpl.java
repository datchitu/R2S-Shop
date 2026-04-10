package com.r2s.R2Sshop.service.impl;

import com.r2s.R2Sshop.DTO.ProductDTORequest;
import com.r2s.R2Sshop.constants.ResponseCode;
import com.r2s.R2Sshop.model.Category;
import com.r2s.R2Sshop.model.Product;
import com.r2s.R2Sshop.repository.CategoryRepository;
import com.r2s.R2Sshop.repository.ProductRepository;
import com.r2s.R2Sshop.rest.AppException;
import com.r2s.R2Sshop.service.CategoryService;
import com.r2s.R2Sshop.service.ProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Service
public class ProductServiceImpl implements ProductService {
    @Autowired
    private ProductRepository productRepository;
    @Autowired
    private CategoryService categoryService;
    @Autowired
    private CategoryRepository categoryRepository;

    /**
     * Return product by id.
     * <p>
     * This function returns product by id, with the id as the input parameter
     * @param id
     * @return Information of Product by id
     * @throws AppException(ResponseCode.PRODUCT_NOT_FOUND) if the Product cannot be found by id
     * @author HoangVu
     * @since 1.1
     */
    @Override
    public Product findById(Long id) {
        return productRepository.findById(id)
                .orElseThrow(() -> new AppException(ResponseCode.PRODUCT_NOT_FOUND));
    }

    /**
     * Return all products by category id and deleted.
     * <p>
     * This function returns all product by categoryId and deleted
     * (With the passed-in status -1, return all by CategoryId works;
     * with 0, return all by CategoryId and deleted == false works;
     * and otherwise, it's return all by CategoryId and deleted == true),
     * with the categoryId, status, pageable as the input parameter
     * and pagination is applied.
     * @param categoryId
     * @param status
     * @param pageable
     * @return Product list by categoryId and pagination is applied
     * @author HoangVu
     * @since 1.2
     */
    @Override
    public Page<Product> findAllByCategoryIdAndDeleted(Long categoryId, Integer status, Pageable pageable) {
        if (!categoryRepository.existsById(categoryId)) {
            throw new AppException(ResponseCode.NOT_FOUND);
        }
        if (status == -1) {
            return productRepository.findAllByCategory_IdAndDeleted(categoryId, null, pageable);
        } else if (status == 0) {
            return productRepository.findAllByCategory_IdAndDeleted(categoryId, false, pageable);
        } else {
            return productRepository.findAllByCategory_IdAndDeleted(categoryId, true, pageable);
        }
    }
    /**
     * Add new product with category.
     * <p>
     * This function is used to add a new product with category.
     * @param dtoRequest
     * @param categoryId
     * @return information of product if the add process is successful
     * @throws AppException(ResponseCode.CATEGORY_NOT_FOUND) if product does not exist in the database
     * @throws AppException(ResponseCode.PRODUCT_ALREADY_EXISTS) if product already been deleted in the database
     * @author HoangVu
     * @since 1.0
     */
    @Override
    public Product addByCategoryId(Long categoryId, ProductDTORequest dtoRequest) {
        Category foundCategory = categoryService.findById(categoryId);
        if (productRepository.existsByNameAndCategoryId(dtoRequest.getName(), categoryId)) {
            throw new AppException(ResponseCode.PRODUCT_ALREADY_EXISTS);
        }
        Product product = new Product();
        product.setName(dtoRequest.getName());
        product.setDeleted(false);
        product.setCategory(foundCategory);
        return productRepository.save(product);
    }
    /**
     * Update product by id.
     * <p>
     * This function updates product by id, with the id as the input parameter.
     * @param id
     * @param categoryId
     * @param dtoRequest
     * @return product by id and userName if the update process is successful
     * @throws AppException(ResponseCode.CATEGORY_NOT_FOUND) if product does not exist in the database
     * @throws AppException(ResponseCode.PRODUCT_NOT_FOUND) if product does not exist in the database
     * @throws AppException(ResponseCode.PRODUCT_ALREADY_EXISTS) if product already been deleted in the database
     * @author HoangVu
     * @since 1.0
     */
    @Override
    public Product updateById(Long id, Long categoryId, ProductDTORequest dtoRequest) {
        Category foundCategory = categoryService.findById(categoryId);
        if (productRepository.existsByNameAndCategoryId(dtoRequest.getName(), categoryId)) {
            throw new AppException(ResponseCode.PRODUCT_ALREADY_EXISTS);
        }
        Product product = findById(id);
        product.setName(dtoRequest.getName());
        product.setCategory(foundCategory);
        return productRepository.save(product);
    }
    /**
     * Delete product by id.
     * <p>
     * This function delete product by id, with the id as the input parameter.
     * @param id
     * @return product by id if the delete process is successful
     * @throws AppException(ResponseCode.PRODUCT_NOT_FOUND) if product does not exist in the database
     * @throws AppException(ResponseCode.DATA_ALREADY_DELETED)
     * if product already been deleted in the database
     * @author HoangVu
     * @since 1.0
     */
    @Override
    public Product deleteById(Long id) {
        Product foundProduct = findById(id);
        if (Boolean.TRUE.equals(foundProduct.getDeleted())) {
            throw new AppException(ResponseCode.DATA_ALREADY_DELETED);
        }
        foundProduct.setDeleted(true);
        return productRepository.save(foundProduct);
    }
    /**
     * Reactivate product by id.
     * <p>
     * This function reactivate product by id, with the id as the input parameter.
     * @param id
     * @return product by id if the reactivate process is successful
     * @throws AppException(ResponseCode.PRODUCT_NOT_FOUND) if product does not exist in the database
     * @throws AppException(ResponseCode.DATA_ALREADY_REACTIVATED)
     * if product already been reactivated in the database
     * @author HoangVu
     * @since 1.0
     */
    @Override
    public Product reactivateById(Long id) {
        Product foundProduct = findById(id);
        if (Boolean.FALSE.equals(foundProduct.getDeleted())) {
            throw new AppException(ResponseCode.DATA_ALREADY_DELETED);
        }
        foundProduct.setDeleted(false);
        return productRepository.save(foundProduct);
    }
}
