package com.r2s.R2Sshop.service;

import com.r2s.R2Sshop.model.VariantProduct;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface VariantProductService {
    Page<VariantProduct> findAllByProductIdAndDeleted(Long productId, Integer status,
                                                      Pageable pageable);
}
