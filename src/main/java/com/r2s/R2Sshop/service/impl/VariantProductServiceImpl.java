package com.r2s.R2Sshop.service.impl;

import com.r2s.R2Sshop.model.VariantProduct;
import com.r2s.R2Sshop.repository.VariantProductRepository;
import com.r2s.R2Sshop.service.VariantProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class VariantProductServiceImpl implements VariantProductService {
    @Autowired
    private VariantProductRepository variantProductRepository;

    /**
     * Return all variant products by product id and deleted.
     * <p>
     * This function returns all variant product by productId and deleted,
     * with the productId, deleted as the input parameter
     * and pagination is applied.
     * @param productId
     * @return All variant product by productId and deleted
     * @author HoangVu
     * @since 1.0
     */
    @Override
    public Page<VariantProduct> findAllByProductIdAndDeleted(Long productId, Boolean deleted, Pageable pageable) {
        return this.variantProductRepository.findAllByProduct_IdAndDeleted(productId, deleted, pageable);
    }


}
