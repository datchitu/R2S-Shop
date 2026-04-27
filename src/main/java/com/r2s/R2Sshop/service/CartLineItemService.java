package com.r2s.R2Sshop.service;

import com.r2s.R2Sshop.DTO.CartLineItemDTORequest;
import com.r2s.R2Sshop.model.Cart;
import com.r2s.R2Sshop.model.CartLineItem;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface CartLineItemService {
    CartLineItem findById(Long id);
    CartLineItem findByIdAndUserName(Long id, String userName);
    Page<CartLineItem> findAllByCartIdAndDeleted(Long cartId, Integer status,
                                                 Pageable pageable);
    Page<CartLineItem> myCartLineItem(String userName, Pageable pageable);
    CartLineItem createNewCartLineItem(Long variantProductId, Cart cart);
    CartLineItem addWithVariantProductAndCart(Long variantProductId, Cart cart,
                                    CartLineItemDTORequest dtoRequest);
    CartLineItem addToCartWithProduct(String userName, Long variantProductId,
                                      CartLineItemDTORequest dtoRequest);
    CartLineItem updateById(Long id, String userName, CartLineItemDTORequest dtoRequest);
    void deleteById(Long id, String userName);
}
