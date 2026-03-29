package com.r2s.R2Sshop.service;

import com.r2s.R2Sshop.model.VariantProduct;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface VariantProductService {
    Page<VariantProduct> findAllByProductId(Long productId, Pageable pageable);
    Page<VariantProduct> findAllByProductIdAndDeleted(Long productId, Boolean deleted, Pageable pageable);
}
