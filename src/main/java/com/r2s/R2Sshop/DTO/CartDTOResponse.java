package com.r2s.R2Sshop.DTO;

import com.r2s.R2Sshop.model.Cart;
import com.r2s.R2Sshop.model.CartLineItem;
import lombok.AllArgsConstructor;
import lombok.Data;
import org.springframework.util.ObjectUtils;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Data
@AllArgsConstructor
public class CartDTOResponse {
    private Long id;
    private Double totalPrice;
    private String note;
    private Boolean paymentType;
    private Boolean paymentStatus;
    private Boolean status;
    private Timestamp paidAt;
    private List<Map<String, Object>> cartLineItems;

    /**
     * Customize the output cart information as a JSON file.
     * <p>
     * This function customizes the output cart information, including
     * ID, totalPrice, note, paymentType, paymentStatus, status, paidAt and cartLineItem list as a JSON file.
     * @param cart
     * @author HoangVu
     * @since 1.0
     */
    public CartDTOResponse(Cart cart) {
        this.id = cart.getId();
        this.totalPrice = cart.getTotalPrice();
        this.note = cart.getNote();
        this.paymentType = cart.getPaymentType();
        this.paymentStatus = cart.getPaymentStatus();
        this.status = cart.getStatus();
        this.paidAt = cart.getPaidAt();
        this.cartLineItems = new ArrayList<>();
        if (!ObjectUtils.isEmpty(cart.getCartLineItems())) {
            for (CartLineItem cartLineItem : cart.getCartLineItems()) {
                this.cartLineItems.add(Map.of(
                        "cartLineItemId", cartLineItem.getId(),
                        "cartLineItemsQuantity", cartLineItem.getQuantity(),
                        "cartLineItemsTotalPrice", cartLineItem.getTotalPrice()));
            }
        }
    }
}
