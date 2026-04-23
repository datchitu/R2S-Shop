package com.r2s.R2Sshop.repository;

import com.r2s.R2Sshop.model.OrderItem;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface OrderItemRepository extends JpaRepository<OrderItem, Long> {
    Page<OrderItem> findAllByOrderId(Long orderId, Pageable pageable);
    @Query("SELECT oi FROM OrderItem oi WHERE oi.id = :id " +
            "AND oi.order.user.userName = :userName")
    Optional<OrderItem> findByIdAndUserName(@Param("id") Long id, @Param("userName") String userName);
    @Query("SELECT oi FROM OrderItem oi WHERE oi.order.id = :orderId " +
            "AND oi.order.user.userName = :userName")
    Page<OrderItem> findAllByOrderIdAndUserName(@Param("orderId") Long orderId,
                                                @Param("userName") String userName,
                                                Pageable pageable);
}
