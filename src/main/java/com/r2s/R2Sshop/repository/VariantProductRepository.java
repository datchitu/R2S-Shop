package com.r2s.R2Sshop.repository;

import com.r2s.R2Sshop.model.VariantProduct;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface VariantProductRepository extends JpaRepository<VariantProduct, Long> {
}
