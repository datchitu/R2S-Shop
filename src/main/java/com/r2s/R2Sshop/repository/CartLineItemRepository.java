package com.r2s.R2Sshop.repository;

import com.r2s.R2Sshop.model.CartLineItem;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.util.Optional;


@Repository
public interface CartLineItemRepository extends JpaRepository<CartLineItem, Long> {
    @Query("SELECT cli FROM CartLineItem cli " +
            "WHERE cli.cart.id = :cartId " +
            "AND (:deleted IS NULL OR cli.deleted = :deleted)")
    Page<CartLineItem> findAllByCartIdAndDeleted(@Param("cartId") Long cartId,
                                                        @Param("deleted") Boolean deleted,
                                                        Pageable pageable);
    @Query("SELECT cli FROM CartLineItem cli " +
            "WHERE cli.variantProduct.id =:variantProductId " +
            "AND cli.cart.id = :cartId " +
            "AND (:deleted IS NULL OR cli.deleted = :deleted)")
    Optional<CartLineItem> findByVariantProductIdAndCartIdAndDeleted(@Param("variantProductId")Long variantProductId,
                                                                     @Param("cartId")Long cartId,
                                                                     @Param("deleted")Boolean deleted);
    Optional<CartLineItem> findByVariantProductIdAndCartId(Long variantProductId, Long cartId);
    @Query("SELECT SUM(cli.totalPrice) FROM CartLineItem cli WHERE cli.cart.id = :cartId " +
            "AND cli.deleted = false")
    BigDecimal sumTotalPriceByCartId(@Param("cartId") Long cartId);
}
