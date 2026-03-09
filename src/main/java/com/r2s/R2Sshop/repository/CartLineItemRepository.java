package com.r2s.R2Sshop.repository;

import com.r2s.R2Sshop.model.CartLineItem;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;


@Repository
public interface CartLineItemRepository extends JpaRepository<CartLineItem, Long> {
    List<CartLineItem> findAllByDeleted(boolean isDeleted);
}
