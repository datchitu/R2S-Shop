package com.r2s.R2Sshop.service.impl;

import com.r2s.R2Sshop.constants.ResponseCode;
import com.r2s.R2Sshop.model.VariantProduct;
import com.r2s.R2Sshop.repository.ProductRepository;
import com.r2s.R2Sshop.repository.VariantProductRepository;
import com.r2s.R2Sshop.rest.AppException;
import com.r2s.R2Sshop.service.VariantProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Service
public class VariantProductServiceImpl implements VariantProductService {
    @Autowired
    private ProductRepository productRepository;
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
}
