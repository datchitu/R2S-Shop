package com.r2s.R2Sshop.DTO;

import com.r2s.R2Sshop.model.CartLineItem;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.util.ObjectUtils;

import java.math.BigDecimal;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CartLineItemSimpleDTOResponse {
    private Integer quantity;
    private BigDecimal totalPrice;
    /**
     * Customize the output cartLineItem information as a JSON file.
     * <p>
     * This method customizes the output cartLineItem information, including
     * quantity and totalPrice as a JSON file.
     * @param cartLineItem
     * @author HoangVu
     * @since 1.0
     */
    public CartLineItemSimpleDTOResponse(CartLineItem cartLineItem) {
        this.quantity = cartLineItem.getQuantity();
        this.totalPrice = cartLineItem.getTotalPrice();
    }
}
