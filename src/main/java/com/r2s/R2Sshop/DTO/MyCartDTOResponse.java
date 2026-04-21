package com.r2s.R2Sshop.DTO;

import com.r2s.R2Sshop.model.Cart;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class MyCartDTOResponse {
    private BigDecimal totalPrice;
    private String note;
    private Boolean paymentType;
    private Boolean paymentStatus;
    private Boolean status;
    private LocalDateTime paidAt;

    /**
     * Customize the output cart information as a JSON file
     * <p>
     * This function customizes the output cart information, including
     * ID, totalPrice, note, paymentType, paymentStatus, status and paidAt as a JSON file.
     * @param cart
     * @author HoangVu
     * @since 1.0
     */
    public MyCartDTOResponse(Cart cart) {
        this.totalPrice = cart.getTotalPrice();
        this.note = cart.getNote();
        this.paymentType = cart.getPaymentType();
        this.paymentStatus = cart.getPaymentStatus();
        this.status = cart.getStatus();
        this.paidAt = cart.getPaidAt();
    }
}
