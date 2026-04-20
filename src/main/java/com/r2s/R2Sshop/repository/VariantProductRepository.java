package com.r2s.R2Sshop.repository;

import com.r2s.R2Sshop.model.VariantProduct;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface VariantProductRepository extends JpaRepository<VariantProduct, Long> {
    @Query("SELECT vr FROM VariantProduct vr WHERE vr.product.id = :productId " +
            "AND (:deleted IS NULL OR vr.deleted = :deleted)")
    Page<VariantProduct> findAllByProduct_IdAndDeleted(@Param("productId") Long productId,
                                                       @Param("deleted") Boolean deleted,
                                                       Pageable pageable);
    boolean existsByName(String name);
    @Modifying(clearAutomatically = true)
    @Query("UPDATE VariantProduct vp SET vp.quantity = vp.quantity - :quantity " +
            "WHERE vp.id = :id AND vp.quantity >= :quantity")
    int decreaseStock(@Param("id") Long id, @Param("quantity") Integer quantity);
}
