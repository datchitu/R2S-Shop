package com.r2s.R2Sshop.DTO;

import com.r2s.R2Sshop.model.Cart;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.sql.Timestamp;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CartSimpleDTOResponse {
    private Double totalPrice;
    private String note;
    private Boolean paymentType;
    private Boolean paymentStatus;
    private Boolean status;
    private Timestamp createdAt;
    private Timestamp paidAt;

    /**
     * Customize the output cart information as a JSON file
     * <p>
     * This function customizes the output cart information, including
     * totalPrice, note, paymentType, paymentStatus, status, createdAt and paidAt as a JSON file.
     * @param cart
     * @author HoangVu
     * @since 1.0
     */
    public CartSimpleDTOResponse(Cart cart) {
        this.totalPrice = cart.getTotalPrice();
        this.note = cart.getNote();
        this.paymentType = cart.getPaymentType();
        this.paymentStatus = cart.getPaymentStatus();
        this.status = cart.getStatus();
        this.createdAt = cart.getCreatedAt();
        this.paidAt = cart.getPaidAt();
    }
}
