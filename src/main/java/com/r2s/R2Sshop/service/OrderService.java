package com.r2s.R2Sshop.service;


import com.r2s.R2Sshop.DTO.OrderDTORequest;
import com.r2s.R2Sshop.model.Order;

public interface OrderService {
    Order findById(Long id, String userName);
    Order findByIdWithoutAuthor(Long id);
    Order myOrder(Integer status, String userName);
    Order create(String userName, Long cartId, OrderDTORequest dtoRequest, Long addressId);
    Order updateById(Long id, String userName, OrderDTORequest dtoRequest);
    void chargeAddress(Long id, String userName, Long addressId);
    void setStatusDelivery(Long id);
    void cancel(Long id, String userName);
    void reactivateById(Long id, String userName);
    void deleteById(Long id);
    void restoreById(Long id);
}
