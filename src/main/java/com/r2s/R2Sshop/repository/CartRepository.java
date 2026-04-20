package com.r2s.R2Sshop.repository;

import com.r2s.R2Sshop.model.Cart;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface CartRepository extends JpaRepository<Cart, Long> {
    Optional<Cart> findByPaymentStatusAndUserUserName(Boolean paymentStatus, String userName);
}
