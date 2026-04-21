package com.r2s.R2Sshop.repository;

import com.r2s.R2Sshop.model.Order;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface OrderRepository extends JpaRepository<Order, Long> {
    Optional<Order> findByStatusAndUserUserName(Boolean status, String userName);
}
