package com.r2s.R2Sshop.service;

import com.r2s.R2Sshop.model.Cart;
import com.r2s.R2Sshop.model.User;

public interface CartService {
    Cart addCart(User user);
    Cart findById(Long id);
    Cart myCart(String userName);
}
