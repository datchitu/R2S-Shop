package com.r2s.R2Sshop.service;

import com.r2s.R2Sshop.model.Cart;
import com.r2s.R2Sshop.model.User;

import java.math.BigDecimal;

public interface CartService {
    Cart addCart(User user);
    Cart findById(Long id);
    Cart myCart(String userName);
    BigDecimal updateTotalPrice(Long id);
    void paymentByCard(String userName, String note, Long userVoucherId);
    void paymentByCash(Long id, String note, Long userVoucherId);
    void setStatus(Long id);
}
