package com.r2s.R2Sshop.repository;

import com.r2s.R2Sshop.model.CartLineItem;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;


@Repository
public interface CartLineItemRepository extends JpaRepository<CartLineItem, Long> {
}
