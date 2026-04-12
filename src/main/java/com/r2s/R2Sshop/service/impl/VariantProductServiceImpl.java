package com.r2s.R2Sshop.service.impl;

import com.r2s.R2Sshop.DTO.VariantServiceDTORequest;
import com.r2s.R2Sshop.constants.ResponseCode;
import com.r2s.R2Sshop.model.Product;
import com.r2s.R2Sshop.model.VariantProduct;
import com.r2s.R2Sshop.repository.ProductRepository;
import com.r2s.R2Sshop.repository.VariantProductRepository;
import com.r2s.R2Sshop.rest.AppException;
import com.r2s.R2Sshop.service.ProductService;
import com.r2s.R2Sshop.service.VariantProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.Objects;

@Service
public class VariantProductServiceImpl implements VariantProductService {
    @Autowired
    private ProductRepository productRepository;
    @Autowired
    private ProductService productService;
    @Autowired
    private VariantProductRepository variantProductRepository;

    /**
     * Return all variant products by product id and deleted.
     * <p>
     * This function returns all variant product by productId and deleted
     * (With the passed-in status -1, return all by productId works;
     * with 0, return all by productId and deleted == false works;
     * and otherwise, it's return all by product id and deleted == true),
     * with the productId, status as the input parameter
     * and pagination is applied.
     * @param productId
     * @param status (-1, 0, 1)
     * @param pageable
     * @return All variant product by productId and deleted
     * @throws AppException(ResponseCode.NOT_FOUND) if the Product cannot be found by productId
     * based on the passed-in ID parameter
     * @author HoangVu
     * @since 1.1
     */
    @Override
    public Page<VariantProduct> findAllByProductIdAndDeleted(Long productId, Integer status,
                                                             Pageable pageable) {
        if (!productRepository.existsById(productId)) {
            throw new AppException(ResponseCode.NOT_FOUND);
        }
        if (status == -1) {
            return variantProductRepository.findAllByProduct_IdAndDeleted(productId,
                    null, pageable);
        } else if (status == 0) {
            return variantProductRepository.findAllByProduct_IdAndDeleted(productId,
                    false, pageable);
        } else {
            return variantProductRepository.findAllByProduct_IdAndDeleted(productId,
                    true, pageable);
        }
    }
    /**
     * Return variant product by id.
     * <p>
     * This function returns variant product by id, with the id as the input parameter
     * @param id
     * @return Information of variant product by id
     * @throws AppException(ResponseCode.VARIANT_PRODUCT_NOT_FOUND)
     * if the variant product cannot be found by id
     * @author HoangVu
     * @since 1.0
     */
    @Override
    public VariantProduct findById(Long id) {
        return variantProductRepository.findById(id)
                .orElseThrow(() -> new AppException(ResponseCode.VARIANT_PRODUCT_NOT_FOUND));
    }
    /**
     * Add new variant product with product.
     * <p>
     * This function is used to add a new variant product with product.
     * @param dtoRequest
     * @param productId
     * @return information of variant product if the add process is successful
     * @throws AppException(ResponseCode.PRODUCT_NOT_FOUND) if product does not exist in the database
     * @throws AppException(ResponseCode.VARIANT_PRODUCT_ALREADY_EXISTS)
     * if variant product already been deleted in the database
     * @author HoangVu
     * @since 1.0
     */
    @Override
    public VariantProduct addByProductId(Long productId, VariantServiceDTORequest dtoRequest) {
        Product foundProduct = productService.findById(productId);
        if (variantProductRepository.existsByName(dtoRequest.getName())) {
            throw new AppException(ResponseCode.VARIANT_PRODUCT_ALREADY_EXISTS);
        }
        VariantProduct variantProduct = new VariantProduct();
        variantProduct.setName(dtoRequest.getName());
        variantProduct.setPrice(dtoRequest.getPrice());
        variantProduct.setColor(dtoRequest.getColor());
        variantProduct.setModelYear(dtoRequest.getModelYear());
        variantProduct.setQuantity(dtoRequest.getQuantity());
        variantProduct.setDeleted(false);
        variantProduct.setProduct(foundProduct);
        return variantProductRepository.save(variantProduct);
    }
    /**
     * Update variant product by id.
     * <p>
     * This function updates variant product by id, with the id as the input parameter.
     * @param id
     * @param productId
     * @param dtoRequest
     * @return variant product by id if the update process is successful
     * @throws AppException(ResponseCode.PRODUCT_NOT_FOUND) if product does not exist in the database
     * @throws AppException(ResponseCode.VARIANT_PRODUCT_NOT_FOUND)
     * if variant product does not exist in the database
     * @throws AppException(ResponseCode.DATA_ALREADY_DELETED)
     * if variant product already been deleted in the database
     * @throws AppException(ResponseCode.IMMUTABLE)
     * if the name, price, color, modelYear, quantity and product remains unchanged
     * @throws AppException(ResponseCode.VARIANT_PRODUCT_ALREADY_EXISTS)
     * if variant product already been deleted in the database
     * @author HoangVu
     * @since 1.1
     */
    @Override
    public VariantProduct updateById(Long id, Long productId, VariantServiceDTORequest dtoRequest) {
        Product foundProduct = productService.findById(productId);
        VariantProduct foundVariantProduct = findById(id);
        if (Boolean.TRUE.equals(foundVariantProduct.getDeleted())) {
            throw new AppException(ResponseCode.DATA_ALREADY_DELETED);
        }
        if (Objects.equals(foundVariantProduct.getName(), dtoRequest.getName()) &&
            Objects.equals(foundVariantProduct.getPrice(), dtoRequest.getPrice()) &&
            Objects.equals(foundVariantProduct.getColor(), dtoRequest.getColor()) &&
            Objects.equals(foundVariantProduct.getModelYear(), dtoRequest.getModelYear()) &&
            Objects.equals(foundVariantProduct.getQuantity(), dtoRequest.getQuantity()) &&
            Objects.equals(foundVariantProduct.getProduct(), foundProduct)) {
            throw new AppException(ResponseCode.IMMUTABLE);
        }
        if (!Objects.equals(foundVariantProduct.getName(), dtoRequest.getName())) {
            if (variantProductRepository.existsByName(dtoRequest.getName())) {
                throw new AppException(ResponseCode.VARIANT_PRODUCT_ALREADY_EXISTS);
            }
        }
        Double price = dtoRequest.getPrice();
        Integer quantity = dtoRequest.getQuantity();
        foundVariantProduct.setName(dtoRequest.getName());
        foundVariantProduct.setPrice(price);
        foundVariantProduct.setColor(dtoRequest.getColor());
        foundVariantProduct.setModelYear(dtoRequest.getModelYear());
        foundVariantProduct.setQuantity(quantity);
        foundVariantProduct.setProduct(foundProduct);
        return variantProductRepository.save(foundVariantProduct);
    }
    /**
     * Delete variant product by id.
     * <p>
     * This function delete variant product by id, with the id as the input parameter.
     * @param id
     * @return variant product by id if the delete process is successful
     * @throws AppException(ResponseCode.VARIANT_PRODUCT_NOT_FOUND)
     * if variant product does not exist in the database
     * @throws AppException(ResponseCode.DATA_ALREADY_DELETED)
     * if variant product already been deleted in the database
     * @author HoangVu
     * @since 1.0
     */
    @Override
    public VariantProduct deleteById(Long id) {
        VariantProduct foundVariantProduct = findById(id);
        if (Boolean.TRUE.equals(foundVariantProduct.getDeleted())) {
            throw new AppException(ResponseCode.DATA_ALREADY_DELETED);
        }
        foundVariantProduct.setDeleted(true);
        return variantProductRepository.save(foundVariantProduct);
    }
    /**
     * Reactivate variant product by id.
     * <p>
     * This function reactivate variant product by id, with the id as the input parameter.
     * @param id
     * @return variant product by id if the reactivate process is successful
     * @throws AppException(ResponseCode.VARIANT_PRODUCT_NOT_FOUND)
     * if variant product does not exist in the database
     * @throws AppException(ResponseCode.DATA_ALREADY_REACTIVATED)
     * if variant product already been reactivated in the database
     * @author HoangVu
     * @since 1.0
     */
    @Override
    public VariantProduct reactivateById(Long id) {
        VariantProduct foundVariantProduct = findById(id);
        if (Boolean.FALSE.equals(foundVariantProduct.getDeleted())) {
            throw new AppException(ResponseCode.DATA_ALREADY_REACTIVATED);
        }
        foundVariantProduct.setDeleted(false);
        return variantProductRepository.save(foundVariantProduct);
    }
}
