package com.r2s.R2Sshop.DTO;

import com.r2s.R2Sshop.model.Order;
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
public class OrderDTOResponse {
    private Long id;
    private String note;
    private LocalDateTime deliveryTime;
    private Boolean deliveryStatus;
    private Double discount;
    private Boolean status;
    private LocalDateTime datetime;
    private BigDecimal totalPrice;
    List<OrderItemDTOResponse> orderItems;
    /**
     * Customize the output order information as a JSON file.
     * <p>
     * This method customizes the output order information, including
     * id, note, deliveryTime, deliveryStatus, discount, status, datetime,
     * totalPrice and list orderItems as a JSON file.
     * @param order
     * @author HoangVu
     * @since 1.2
     */
    public OrderDTOResponse(Order order) {
        this.id = order.getId();
        this.note = order.getNote();
        this.deliveryTime = order.getDeliveryTime();
        this.deliveryStatus = order.getDeliveryStatus();
        this.discount = order.getDiscount();
        this.status = order.getStatus();
        this.datetime = order.getOrderDatetime();
        this.totalPrice = order.getTotalPrice();
        if (!ObjectUtils.isEmpty(order.getOrderItems())) {
            this.orderItems = order.getOrderItems().stream()
                    .map(OrderItemDTOResponse :: new)
                    .collect(Collectors.toList());
        }
    }
}
