package com.r2s.R2Sshop.service.impl;

import com.r2s.R2Sshop.DTO.CartLineItemDTORequest;
import com.r2s.R2Sshop.constants.ResponseCode;
import com.r2s.R2Sshop.model.Cart;
import com.r2s.R2Sshop.model.CartLineItem;
import com.r2s.R2Sshop.model.VariantProduct;
import com.r2s.R2Sshop.repository.CartLineItemRepository;
import com.r2s.R2Sshop.rest.AppException;
import com.r2s.R2Sshop.service.CartLineItemService;
import com.r2s.R2Sshop.service.CartService;
import com.r2s.R2Sshop.service.UserService;
import com.r2s.R2Sshop.service.VariantProductService;
import jakarta.transaction.Transactional;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;

@Service
public class CartLineItemServiceImpl implements CartLineItemService {
    @Autowired
    private CartLineItemRepository cartLineItemRepository;
    @Autowired
    private VariantProductService variantProductService;
    @Autowired
    private CartService cartService;
    @Autowired
    private UserService userService;
    @Autowired
    private ModelMapper modelMapper;

    /**
     * Return cartLineItem by id.
     * <p>
     * This method returns cartLineItem by id, with the id as the input parameter
     * @param id
     * @return Information of cartLineItem by id
     * @throws AppException(ResponseCode.CART_LINE_ITEM_NOT_FOUND)
     * if the cartLineItem cannot be found by id
     * @author HoangVu
     * @since 1.0
     */
    @Override
    public CartLineItem findById(Long id) {
        return cartLineItemRepository.findById(id)
                .orElseThrow(() -> new AppException(ResponseCode.CART_LINE_ITEM_NOT_FOUND));
    }
    /**
     * Return cartLineItem list by deleted.
     * <p>
     * This method returns all cartLineItem by cartId and deleted
     * (With the passed-in status -1, return all works;
     * with 0, return all by deleted == false works;
     * and otherwise, it's return all by deleted == true),
     * with the status and pageable as the input parameter.
     * @param cartId
     * @param status
     * @param pageable
     * @return cartLineItem list by cartId and deleted
     * @author HoangVu
     * @since 1.0
     */
    @Override
    public Page<CartLineItem> findAllByCartIdAndDeleted(Long cartId, Integer status,
                                                        Pageable pageable) {
        if (status == -1) {
            return cartLineItemRepository
                    .findAllByCartIdAndDeleted(cartId,null, pageable);
        } else if (status == 0) {
            return cartLineItemRepository
                    .findAllByCartIdAndDeleted(cartId, true, pageable);
        } else {
            return cartLineItemRepository
                    .findAllByCartIdAndDeleted(cartId, false, pageable);
        }
    }
    /**
     * Return my cartLineItem list.
     * <p>
     * This method returns cartLineItem list by cartId and pageable,
     * with userName, pageable as the input parameter.
     * @param userName
     * @param pageable
     * @return cartLineItem list by cartId and deleted
     * @author HoangVu
     * @since 1.0
     */
    @Override
    public Page<CartLineItem> myCartLineItem(String userName, Pageable pageable){
        Cart foundCart = cartService.myCart(userName);
        return cartLineItemRepository.findAllByCartIdAndDeleted(foundCart.getId(),
                false, pageable);
    }
    /**
     * Create new cartLineItem.
     * <p>
     * This method is used to create a new cartLineItem.
     * @param variantProductId
     * @param cart
     * @return information of cartLineItem if the create process is successful
     * @author HoangVu
     * @since 1.0
     */
    @Override
    public CartLineItem createNewCartLineItem(Long variantProductId, Cart cart) {
        VariantProduct foundVariantProduct = variantProductService.findById(variantProductId);
        CartLineItem newCartLineItem = new CartLineItem();
        newCartLineItem.setVariantProduct(foundVariantProduct);
        newCartLineItem.setCart(cart);
        return newCartLineItem;
    }
    /**
     * Add new cartLineItem.
     * <p>
     * This method is used to add a new cartLineItem.
     * If cartLineItem does not exist create new cartLineItem.
     * If cartLineItem already exists check deleted (
     * with deleted == false, update quantity and totalPrice, else set newQuantity,
     * totalPrice and set false for deleted)
     * @param variantProductId
     * @param dtoRequest
     * @param cart
     * @return information of cartLineItem if the add process is successful
     * @author HoangVu
     * @since 1.0
     */
    @Override
    @Transactional
    public CartLineItem addWithVariantProductAndCart(Long variantProductId, Cart cart,
                                           CartLineItemDTORequest dtoRequest) {
        CartLineItem cartLineItem = cartLineItemRepository.findByVariantProductIdAndCartId(
                variantProductId, cart.getId())
                .orElseGet(() -> createNewCartLineItem(variantProductId, cart));

        BigDecimal newQuantity = new BigDecimal(dtoRequest.getQuantity());

        if (!cartLineItem.isNew() && Boolean.FALSE.equals(cartLineItem.getDeleted())) {
            newQuantity = newQuantity.add(new BigDecimal(cartLineItem.getQuantity()));
        }

        cartLineItem.setDeleted(false);
        cartLineItem.setQuantity(newQuantity.intValue());
        cartLineItem.setTotalPrice(newQuantity.multiply(cartLineItem.getVariantProduct().getPrice()));

        return cartLineItemRepository.save(cartLineItem);
    }
    /**
     * Add new cartLineItem to cart with product.
     * <p>
     * This method is used to add a new cartLineItem to cart with product.
     * @param dtoRequest
     * @return information of cartLineItem if the add process is successful
     * @author HoangVu
     * @since 1.1
     */
    @Override
    @Transactional
    public CartLineItem addToCartWithProduct(String userName, Long variantProductId,
                                             CartLineItemDTORequest dtoRequest) {
        Cart foundCart = cartService.myCart(userName);
        CartLineItem cartLineItem = addWithVariantProductAndCart(variantProductId, foundCart, dtoRequest);
        cartService.updateTotalPrice(foundCart.getId());
        return cartLineItem;
    }
    /**
     * Update cartLineItem by id.
     * <p>
     * This method updates cartLineItem by id, with the id as the input parameter.
     * @param id
     * @param dtoRequest
     * @return cartLineItem by id if the update process is successful
     * @throws AppException(ResponseCode.CART_LINE_ITEM_NOT_FOUND) if cartLineItem does not exist in the database
     * @throws AppException(ResponseCode.CART_LINE_ITEM_ALREADY_DELETED)
     * if cartLineItem already been deleted in the database
     * @author HoangVu
     * @since 1.1
     */
    @Transactional
    @Override
    public CartLineItem updateById(Long id, CartLineItemDTORequest dtoRequest) {
        CartLineItem foundCartLineItem = findById(id);
        if (Boolean.TRUE.equals(foundCartLineItem.getDeleted())) {
            throw new AppException(ResponseCode.CART_LINE_ITEM_ALREADY_DELETED);
        }
        BigDecimal newQuantity = new BigDecimal(dtoRequest.getQuantity());
        VariantProduct foundVariantProduct = variantProductService
                .findById(foundCartLineItem.getVariantProduct().getId());
        modelMapper.map(dtoRequest, foundCartLineItem);
        foundCartLineItem.setTotalPrice(newQuantity.multiply(foundVariantProduct.getPrice()));
        return cartLineItemRepository.save(foundCartLineItem);
    }
    /**
     * Delete cartLineItem by id.
     * <p>
     * This method deletes cartLineItem by id, with the id as the input parameter.
     * @param id
     * @throws AppException(ResponseCode.VOUCHER_NOT_FOUND)
     * if cartLineItem does not exist in the database
     * @throws AppException(ResponseCode.CART_LINE_ITEM_ALREADY_DELETED)
     * if cartLineItem already been deleted in the database
     * @author HoangVu
     * @since 1.0
     */
    @Override
    public void deleteById(Long id) {
        CartLineItem foundCartLineItem = findById(id);
        if (Boolean.TRUE.equals(foundCartLineItem.getDeleted())) {
            throw new AppException(ResponseCode.CART_LINE_ITEM_ALREADY_DELETED);
        }
        foundCartLineItem.setQuantity(0);
        foundCartLineItem.setTotalPrice(BigDecimal.ZERO);
        foundCartLineItem.setDeleted(true);
        cartLineItemRepository.save(foundCartLineItem);
    }
}
