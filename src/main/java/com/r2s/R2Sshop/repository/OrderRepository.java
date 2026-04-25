package com.r2s.R2Sshop.repository;

import com.r2s.R2Sshop.model.Order;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface OrderRepository extends JpaRepository<Order, Long> {
    @Query("SELECT o FROM Order o WHERE o.user.userName = :userName " +
            "AND (:status IS NULL OR o.status = :status)")
    Page<Order> findByStatusAndUserUserName(@Param("status") Boolean status,
                                            @Param("userName") String userName,
                                            Pageable pageable);
    boolean findByCartId(Long cartId);

    Optional<Order> findByIdAndUserUserName(Long id, String userName);

    @Query("SELECT o FROM Order o " +
            "WHERE (:deliveryStatus IS NULL OR o.deliveryStatus = :deliveryStatus)")
    Page<Order> findAllByDeliveryStatus(@Param("deliveryStatus") Boolean deliveryStatus, Pageable pageable);

    @Query("SELECT o FROM Order o WHERE o.user.userName = :userName " +
            "AND (:deliveryStatus IS NULL OR o.deliveryStatus = :deliveryStatus)")
    Page<Order> findByDeliveryStatusAndUserName(@Param("deliveryStatus") Boolean deliveryStatus,
                                                @Param("userName") String userName,
                                                Pageable pageable);
}
