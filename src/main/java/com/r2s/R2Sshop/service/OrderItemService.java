package com.r2s.R2Sshop.service;

import com.r2s.R2Sshop.model.OrderItem;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface OrderItemService {
    OrderItem findById(Long id, String userName);
    Page<OrderItem> findAllByOrderId(Long orderId, Pageable pageable, String userName);
}
