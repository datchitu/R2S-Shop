package com.r2s.R2Sshop.service.impl;

import com.r2s.R2Sshop.model.Cart;
import com.r2s.R2Sshop.model.User;
import com.r2s.R2Sshop.repository.CartRepository;
import com.r2s.R2Sshop.service.CartService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class CartServiceImpl implements CartService {
    @Autowired
    private CartRepository cartRepository;

    /**
     * Add new cart.
     * <p>
     * This function is used to add a new cart.
     * @param user
     * @return cartRepository.save(cart)
     * @author HoangVu
     * @since 1.1
     */
    @Override
    public Cart addCart(User user) {
        Cart cart = new Cart();
        cart.setUser(user);
        return this.cartRepository.save(cart);
    }
}
