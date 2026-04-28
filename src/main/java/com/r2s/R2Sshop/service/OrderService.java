package com.r2s.R2Sshop.service;


import com.r2s.R2Sshop.DTO.OrderDTORequest;
import com.r2s.R2Sshop.model.Cart;
import com.r2s.R2Sshop.model.Order;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface OrderService {
    Order findById(Long id, String userName);
    Page<Order> findAllByDeliveryStatus(Integer status, Pageable pageable);
    Page<Order> findAllByDeliveryStatusAndUserName(Integer status, String userName,
                                                   Pageable pageable);
    Page<Order> myOrder(Integer status, String userName, Pageable pageable);
    void create(String userName, Cart cart, OrderDTORequest dtoRequest,
                Long addressId, Double discount);
    Order updateById(Long id, String userName, OrderDTORequest dtoRequest);
    void chargeAddress(Long id, String userName, Long addressId);
    void setDeliveryStatus(Long id);
    void cancelById(Long id, String userName);
    void reactivateById(Long id, String userName);
    void deleteById(Long id);
    void restoreById(Long id);
}
