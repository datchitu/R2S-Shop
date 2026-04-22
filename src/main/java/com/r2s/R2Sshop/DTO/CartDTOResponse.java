package com.r2s.R2Sshop.DTO;

import com.r2s.R2Sshop.model.Cart;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.util.ObjectUtils;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CartDTOResponse {
    private BigDecimal totalPrice;
    private Boolean paymentType;
    private Boolean paymentStatus;
    private Boolean status;
    private LocalDateTime paidAt;
    private List<CartLineItemSimpleDTOResponse> cartLineItems;

    /**
     * Customize the output cart information as a JSON file
     * <p>
     * This method customizes the output cart information, including
     * ID, totalPrice, paymentType, paymentStatus, status, paidAt and cartLineItem list as a JSON file.
     * @param cart
     * @author HoangVu
     * @since 1.3
     */
    public CartDTOResponse(Cart cart) {
        this.totalPrice = cart.getTotalPrice();
        this.paymentType = cart.getPaymentType();
        this.paymentStatus = cart.getPaymentStatus();
        this.status = cart.getStatus();
        this.paidAt = cart.getPaidAt();
        if (!ObjectUtils.isEmpty(cart.getCartLineItems())) {
            this.cartLineItems = cart.getCartLineItems().stream()
                    .map(CartLineItemSimpleDTOResponse::new)
                    .collect(Collectors.toList());
        }
    }
}
