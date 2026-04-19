package com.r2s.R2Sshop.service.impl;

import com.r2s.R2Sshop.constants.ResponseCode;
import com.r2s.R2Sshop.model.Cart;
import com.r2s.R2Sshop.model.User;
import com.r2s.R2Sshop.repository.CartRepository;
import com.r2s.R2Sshop.rest.AppException;
import com.r2s.R2Sshop.service.CartService;
import com.r2s.R2Sshop.service.UserService;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class CartServiceImpl implements CartService {
    @Autowired
    private CartRepository cartRepository;
    @Autowired
    private UserService userService;

    /**
     * Add new cart.
     * <p>
     * This function is used to add a new cart.
     * @param user
     * @return cartRepository.save(cart)
     * @author HoangVu
     * @since 1.2
     */
    @Transactional
    @Override
    public Cart addCart(User user) {
        Cart cart = new Cart();
        cart.setUser(user);
        return cartRepository.save(cart);
    }
    /**
     * Return cart by id.
     * <p>
     * This function returns cart by id, with the id as the input parameter.
     * @param id
     * @return cart by id
     * @throws AppException(ResponseCode.CART_NOT_FOUND) if the cart cannot be found by id
     * @author HoangVu
     * @since 1.0
     */
    @Override
    public Cart findById(Long id) {
        return cartRepository.findById(id)
                .orElseThrow(() -> new AppException(ResponseCode.CART_NOT_FOUND));
    }
    /**
     * Return cart by userName.
     * <p>
     * This function returns cart by userName, with the userName as the input parameter
     * if found by paymentStatus = false and userName. Opposite, it returns new cart.
     * @param userName
     * @return cart by userName
     * @throws AppException(ResponseCode.USER_NOT_FOUND)
     * @author HoangVu
     * @since 1.0
     */
    @Transactional
    @Override
    public Cart myCart(String userName) {
        User foundUser = userService.findByUserName(userName);
        return cartRepository.findByPaymentStatusAndUserUserName(false, userName)
                .orElseGet(() -> {
                    Cart cart = new Cart();
                    cart.setUser(foundUser);
                    return cartRepository.save(cart);
                });
    }

}
