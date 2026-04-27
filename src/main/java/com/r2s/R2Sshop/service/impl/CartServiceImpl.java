package com.r2s.R2Sshop.service.impl;

import com.r2s.R2Sshop.DTO.OrderDTORequest;
import com.r2s.R2Sshop.constants.ResponseCode;
import com.r2s.R2Sshop.model.*;
import com.r2s.R2Sshop.repository.*;
import com.r2s.R2Sshop.rest.AppException;
import com.r2s.R2Sshop.service.*;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.ObjectUtils;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.List;

@Service
public class CartServiceImpl implements CartService {
    private final Instant instant = Instant.now();
    private final LocalDateTime localDateTime = LocalDateTime.ofInstant(instant, ZoneId.systemDefault());

    @Autowired
    private CartRepository cartRepository;
    @Autowired
    private CartLineItemRepository cartLineItemRepository;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private VariantProductRepository variantProductRepository;
    @Autowired
    private UserVoucherService userVoucherService;
    @Autowired
    private OrderService orderService;

    /**
     * Add new cart.
     * <p>
     * This method is used to add a new cart.
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
     * This method returns cart by id, with the id as the input parameter.
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
     * This method returns cart by userName, with the userName as the input parameter
     * if found by paymentStatus = false and userName. Opposite, it returns new cart.
     * @param userName
     * @return cart by userName
     * @throws AppException(ResponseCode.USER_NOT_FOUND) if user does not found by userName
     * @author HoangVu
     * @since 1.1
     */
    @Transactional
    @Override
    public Cart myCart(String userName) {
        User foundUser = userRepository.findByUserName(userName)
                .orElseThrow(() -> new AppException(ResponseCode.USER_NOT_FOUND));
        return cartRepository.findByPaymentStatusAndUserUserName(false, userName)
                .orElseGet(() -> addCart(foundUser));
    }
    /**
     * UpdateTotalPrice by id.
     * <p>
     * This method updates updateTotalPrice by id, with the id as the input parameter.
     * @param id
     * @throws AppException(ResponseCode.CART_NOT_FOUND) if the cart cannot be found by id
     * @author HoangVu
     * @since 1.0
     */
    @Override
    @Transactional
    public BigDecimal updateTotalPrice(Long id) {
        Cart cart = findById(id);
        BigDecimal totalPrice = cartLineItemRepository.sumTotalPriceByCartId(id);
        cart.setTotalPrice(totalPrice != null ? totalPrice : BigDecimal.ZERO);
        cartRepository.save(cart);
        return totalPrice;
    }
    /**
     * Payment by card and apply voucher (if available) and create order by userName.
     * <p>
     * This method used for payment by card and apply voucher (if available) and create order by userName,
     * with the userName, userVoucherId as the input parameter.
     * @param userName
     * @param userVoucherId
     * @param dtoRequest
     * @param addressId
     * @throws AppException(ResponseCode.CART_NOT_FOUND) if the cart cannot be found by id
     * @throws AppException(ResponseCode.CART_ALREADY_PAID) if the cart has been paid
     * @throws AppException(ResponseCode.CART_IS_EMPTY) if the cart is empty
     * @throws AppException(ResponseCode.ACCESS_DENIED) if voucher is not owned by the user
     * @author HoangVu
     * @since 1.4
     */
    @Override
    @Transactional
    public void paymentByCard(String userName, Long userVoucherId,
                              OrderDTORequest dtoRequest, Long addressId) {
        Cart foundCart = myCart(userName);
        if (Boolean.TRUE.equals(foundCart.getPaymentStatus())) {
            throw new AppException(ResponseCode.CART_ALREADY_PAID);
        }

        if (!cartLineItemRepository.existsByCartIdAndDeleted(foundCart.getId(), false)) {
            throw new AppException(ResponseCode.CART_IS_EMPTY);
        }

        BigDecimal totalPrice = updateTotalPrice(foundCart.getId());

        if (userVoucherId != null) {
            UserVoucher foundUserVoucher = userVoucherService.useById(userVoucherId);
            if (!foundUserVoucher.getUser().getUserName().equals(userName)) {
                throw new AppException(ResponseCode.ACCESS_DENIED);
            }
            BigDecimal discount = BigDecimal.valueOf(foundUserVoucher.getVoucher().getDiscount());
            BigDecimal discountFactor = BigDecimal.ONE.subtract(discount);
            totalPrice = totalPrice.multiply(discountFactor.setScale(2, RoundingMode.HALF_UP));
        }

        foundCart.setPaymentType(true);
        foundCart.setPaymentStatus(true);
        foundCart.setPaidAt(localDateTime);
        foundCart.setTotalPrice(totalPrice);

        cartRepository.saveAndFlush(foundCart);

        orderService.create(userName, foundCart, dtoRequest, addressId);

    }
    /**
     * Payment by cash and apply voucher (if available) and create order by userName.
     * <p>
     * This method used for payment by cash and apply voucher (if available) and create order by userName,
     * with the userName, userVoucherId as the input parameter.
     * @param userName
     * @param userVoucherId
     * @param dtoRequest
     * @param addressId
     * @throws AppException(ResponseCode.CART_NOT_FOUND) if the cart cannot be found by userName
     * @throws AppException(ResponseCode.CART_ALREADY_PAID) if the cart has been paid
     * @throws AppException(ResponseCode.CART_IS_EMPTY) if the cart is empty
     * @throws AppException(ResponseCode.ACCESS_DENIED) if voucher is not owned by the user
     * @author HoangVu
     * @since 1.4
     */
    @Override
    @Transactional
    public void paymentByCash(String userName, Long userVoucherId,
                              OrderDTORequest dtoRequest, Long addressId) {
        Cart foundCart = myCart(userName);
        if (Boolean.TRUE.equals(foundCart.getPaymentStatus())) {
            throw new AppException(ResponseCode.CART_ALREADY_PAID);
        }

        if (!cartLineItemRepository.existsByCartIdAndDeleted(foundCart.getId(), false)) {
            throw new AppException(ResponseCode.CART_IS_EMPTY);
        }

        BigDecimal totalPrice = updateTotalPrice(foundCart.getId());
        if (!ObjectUtils.isEmpty(userVoucherId)) {
            UserVoucher foundUserVoucher = userVoucherService.useById(userVoucherId);
            if (!foundUserVoucher.getUser().getUserName().equals(userName)) {
                throw new AppException(ResponseCode.ACCESS_DENIED);
            }
            BigDecimal discount = BigDecimal.valueOf(foundUserVoucher.getVoucher().getDiscount());
            BigDecimal discountFactor = BigDecimal.ONE.subtract(discount);
            totalPrice = totalPrice.multiply(discountFactor.setScale(2, RoundingMode.HALF_UP)) ;
            foundCart.setUserVoucher(foundUserVoucher);
        }
        foundCart.setPaymentType(false);
        foundCart.setPaymentStatus(true);
        foundCart.setPaidAt(localDateTime);
        foundCart.setTotalPrice(totalPrice);

        cartRepository.saveAndFlush(foundCart);

        orderService.create(userName, foundCart, dtoRequest, addressId);
    }
    /**
     * SetStatus by id.
     * <p>
     * This method sets status and updates quantity of variant product by id, with the id as the input parameter.
     * @param id
     * @throws AppException(ResponseCode.CART_NOT_FOUND) if the cart cannot be found by id
     * @throws AppException(ResponseCode.CART_NOT_YET_PAID) if the cart is not yet paid for
     * @throws AppException(ResponseCode.INSUFFICIENT_STOCK) if the variant product out of stock
     * @author HoangVu
     * @since 1.1
     */
    @Override
    @Transactional
    public void setStatus(Long id) {
        Cart foundCart = findById(id);
        if (Boolean.TRUE.equals(foundCart.getPaymentStatus())) {
            throw new AppException(ResponseCode.CART_NOT_YET_PAID);
        }

        List<CartLineItem> cartLineItems = foundCart.getCartLineItems();
        for (CartLineItem cartLineItem : cartLineItems) {
            int updatedRows = variantProductRepository.decreaseStock(
                    cartLineItem.getVariantProduct().getId(),
                    cartLineItem.getQuantity()
            );

            if (updatedRows == 0) {
//                String variantProductName = cartLineItem.getVariantProduct().getName();
//                throw AppException.(ResponseCode.INSUFFICIENT_STOCK, "VariantProduct " + variantProductName);
                throw new AppException(ResponseCode.INSUFFICIENT_STOCK);
            }

        }

        foundCart.setStatus(true);
        cartRepository.save(foundCart);
    }
}
