package com.r2s.R2Sshop.service;


import com.r2s.R2Sshop.DTO.OrderDTORequest;
import com.r2s.R2Sshop.model.Order;

public interface OrderService {
    Order findById(Long id);
    Order myOrder(Integer status, String userName);
    Order add(OrderDTORequest dtoRequest);
    Order updateById(Long id, OrderDTORequest dtoRequest);
    void setStatusDelivery(Long id);
    void cancel(Long id);
    void reactivateById(Long id);
    void deleteById(Long id);
    void restoreById(Long id);
}
