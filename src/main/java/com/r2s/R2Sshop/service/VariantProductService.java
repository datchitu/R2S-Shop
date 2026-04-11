package com.r2s.R2Sshop.service;

import com.r2s.R2Sshop.DTO.VariantServiceDTORequest;
import com.r2s.R2Sshop.model.VariantProduct;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface VariantProductService {
    Page<VariantProduct> findAllByProductIdAndDeleted(Long productId, Integer status,
                                                      Pageable pageable);
    VariantProduct findById(Long id);
    VariantProduct addByProductId(Long productId, VariantServiceDTORequest dtoRequest);
    VariantProduct updateById(Long id, Long productId, VariantServiceDTORequest dtoRequest);
    VariantProduct deleteById(Long id);
    VariantProduct reactivateById(Long id);
}
