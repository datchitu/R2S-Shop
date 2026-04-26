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
import jakarta.transaction.Transactional;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.Objects;

@Service
public class ProductServiceImpl implements ProductService {
    @Autowired
    private ProductRepository productRepository;
    @Autowired
    private CategoryService categoryService;
    @Autowired
    private CategoryRepository categoryRepository;
    @Autowired
    private ModelMapper modelMapper;

    /**
     * Return product by id.
     * <p>
     * This method returns product by id, with the id as the input parameter
     * @param id
     * @return Information of product by id
     * @throws AppException(ResponseCode.PRODUCT_NOT_FOUND) if the product cannot be found by id
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
     * This method returns all product by categoryId and deleted
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
     * This method is used to add a new product with category.
     * @param dtoRequest
     * @param categoryId
     * @return information of product if the add process is successful
     * @throws AppException(ResponseCode.CATEGORY_NOT_FOUND) if category does not exist in the database
     * @throws AppException(ResponseCode.PRODUCT_ALREADY_EXISTS) if product already been deleted in the database
     * @author HoangVu
     * @since 1.1
     */
    @Transactional
    @Override
    public Product addByCategoryId(Long categoryId, ProductDTORequest dtoRequest) {
        Category foundCategory = categoryService.findById(categoryId);
        if (productRepository.existsByNameAndCategoryId(dtoRequest.getName(), categoryId)) {
            throw new AppException(ResponseCode.PRODUCT_ALREADY_EXISTS);
        }
        Product product = modelMapper.map(dtoRequest, Product.class);
        product.setCategory(foundCategory);
        return productRepository.save(product);
    }
    /**
     * Update product by id.
     * <p>
     * This method updates product by id, with the id as the input parameter.
     * @param id
     * @param categoryId
     * @param dtoRequest
     * @return product by id if the update process is successful
     * @throws AppException(ResponseCode.CATEGORY_NOT_FOUND) if category does not exist in the database
     * @throws AppException(ResponseCode.PRODUCT_NOT_FOUND) if product does not exist in the database
     * @throws AppException(ResponseCode.DATA_ALREADY_DELETED)
     * if product already been deleted in the database
     * @throws AppException(ResponseCode.IMMUTABLE) if the name remains unchanged
     * @throws AppException(ResponseCode.PRODUCT_ALREADY_EXISTS) if product already been deleted in the database
     * @author HoangVu
     * @since 1.2
     */
    @Transactional
    @Override
    public Product updateById(Long id, Long categoryId, ProductDTORequest dtoRequest) {
        Category foundCategory = categoryService.findById(categoryId);
        Product foundProduct = findById(id);
        if (Boolean.TRUE.equals(foundProduct.getDeleted())) {
            throw new AppException(ResponseCode.VARIANT_PRODUCT_ALREADY_DELETED);
        }
        if (Objects.equals(foundProduct.getName(), dtoRequest.getName()) &&
            Objects.equals(foundProduct.getCategory(), foundCategory)){
            throw new AppException(ResponseCode.IMMUTABLE);
        }
        if (productRepository.existsByNameAndCategoryId(dtoRequest.getName(), categoryId)) {
            throw new AppException(ResponseCode.PRODUCT_ALREADY_EXISTS);
        }
        modelMapper.map(dtoRequest, foundProduct);
        foundProduct.setCategory(foundCategory);
        return productRepository.save(foundProduct);
    }
    /**
     * Delete product by id.
     * <p>
     * This method deletes product by id, with the id as the input parameter.
     * @param id
     * @throws AppException(ResponseCode.PRODUCT_NOT_FOUND) if product does not exist in the database
     * @throws AppException(ResponseCode.PRODUCT_ALREADY_DELETED)
     * if product already been deleted in the database
     * @author HoangVu
     * @since 1.2
     */
    @Override
    public void deleteById(Long id) {
        Product foundProduct = findById(id);
        if (Boolean.TRUE.equals(foundProduct.getDeleted())) {
            throw new AppException(ResponseCode.PRODUCT_ALREADY_DELETED);
        }
        foundProduct.setDeleted(true);
        productRepository.save(foundProduct);
    }
    /**
     * Restore product by id.
     * <p>
     * This method restores product by id, with the id as the input parameter.
     * @param id
     * @throws AppException(ResponseCode.PRODUCT_NOT_FOUND) if product does not exist in the database
     * @throws AppException(ResponseCode.PRODUCT_ALREADY_RESTORED)
     * if product already been restored in the database
     * @author HoangVu
     * @since 1.3
     */
    @Override
    public void restoreById(Long id) {
        Product foundProduct = findById(id);
        if (Boolean.FALSE.equals(foundProduct.getDeleted())) {
            throw new AppException(ResponseCode.PRODUCT_ALREADY_RESTORED);
        }
        foundProduct.setDeleted(false);
        productRepository.save(foundProduct);
    }
}
