package com.r2s.R2Sshop.repository;

import com.r2s.R2Sshop.model.Product;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ProductRepository extends JpaRepository<Product, Long> {
    @Query("SELECT p FROM Product p WHERE p.category.id = :categoryId " +
            "AND (:deleted IS NULL OR p.deleted = :deleted)")
    Page<Product> findAllByCategory_IdAndDeleted(@Param("categoryId") Long categoryId,
                                                 @Param("deleted") Boolean deleted,
                                                 Pageable pageable);

    boolean existsByNameAndCategoryId(String name, Long categoryId);
}
