package com.r2s.R2Sshop.DTO;

import com.r2s.R2Sshop.model.Cart;
import com.r2s.R2Sshop.model.CartLineItem;
import com.r2s.R2Sshop.model.VariantProduct;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.util.ObjectUtils;

import java.math.BigDecimal;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CartLineItemDTOResponse
{
    private Integer quantity;
    private BigDecimal totalPrice;
    private String variantProductName;
    private Long cartId;
    /**
     * Customize the output cartLineItem information as a JSON file.
     * <p>
     * This method customizes the output cartLineItem information, including
     * quantity and totalPrice, variantProductName and cartId as a JSON file.
     * @param cartLineItem
     * @author HoangVu
     * @since 1.1
     */
    public CartLineItemDTOResponse(CartLineItem cartLineItem) {
        this.quantity = cartLineItem.getQuantity();
        this.totalPrice = cartLineItem.getTotalPrice();
        if (!ObjectUtils.isEmpty(cartLineItem.getVariantProduct())) {
            this.variantProductName = cartLineItem.getVariantProduct().getName();
        }
        if (!ObjectUtils.isEmpty(cartLineItem.getCart())) {
            this.cartId = cartLineItem.getCart().getId();
        }
    }
}
