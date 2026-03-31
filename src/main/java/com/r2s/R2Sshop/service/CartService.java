package com.r2s.R2Sshop.service;

import com.r2s.R2Sshop.model.Cart;
import com.r2s.R2Sshop.model.User;

import java.util.Map;

public interface CartService {
    Cart addCart(User user);
}
