package com.r2s.R2Sshop.repository;

import com.r2s.R2Sshop.model.VariantProduct;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface VariantProductRepository extends JpaRepository<VariantProduct, Long> {
    Page<VariantProduct> findAllByProduct_Id(Long productId, Pageable pageable);

    Page<VariantProduct> findAllByProduct_IdAndDeleted(Long productId, Boolean deleted, Pageable pageable);
}
