package com.r2s.R2Sshop.service;

import com.r2s.R2Sshop.DTO.OrderItemDTORequest;
import com.r2s.R2Sshop.model.OrderItem;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface OrderItemService {
    OrderItem findById(Long id);
    Page<OrderItem> findAllByOrderId(Long orderId, Pageable pageable);
    OrderItem addByOrderId(Long orderId, OrderItemDTORequest dtoRequest);
}
