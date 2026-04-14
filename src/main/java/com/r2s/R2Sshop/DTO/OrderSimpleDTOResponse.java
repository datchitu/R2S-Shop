package com.r2s.R2Sshop.DTO;

import com.r2s.R2Sshop.model.Order;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class OrderSimpleDTOResponse {
    private Long orderId;
    private Date deliveryTime;
    private Boolean status;
    private Date datetime;
    private Double totalPrice;

    /**
     * Customize the output order information as a JSON file
     * <p>
     * This function customizes the output order information, including
     * orderId, deliveryTime, status, datetime and totalPrice as a JSON file.
     * @param order
     * @author HoangVu
     * @since 1.0
     */
    public OrderSimpleDTOResponse(Order order) {
        this.orderId = order.getId();
        this.deliveryTime = order.getDeliveryTime();
        this.status = order.getStatus();
        this.datetime = order.getOrderDatetime();
        this.totalPrice = order.getTotalPrice();
    }
}
