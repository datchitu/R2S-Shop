package com.r2s.R2Sshop.service;

import com.r2s.R2Sshop.DTO.OrderDTORequest;
import com.r2s.R2Sshop.model.Cart;
import com.r2s.R2Sshop.model.User;

import java.math.BigDecimal;

public interface CartService {
    Cart addCart(User user);
    Cart findById(Long id);
    Cart myCart(String userName);
    BigDecimal updateTotalPrice(Long id);
    void paymentByCard(String userName, Long userVoucherId,
                       OrderDTORequest dtoRequest, Long addressId);
    void paymentByCash(String userName, Long userVoucherId,
                       OrderDTORequest dtoRequest, Long addressId);
    void setStatus(Long id);
}
