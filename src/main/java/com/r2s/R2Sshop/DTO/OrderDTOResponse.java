package com.r2s.R2Sshop.DTO;

import com.r2s.R2Sshop.model.Order;
import com.r2s.R2Sshop.model.OrderItem;
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
    private LocalDateTime deliveryTime;
    private String note;
    private Boolean deliveryStatus;
    private Boolean status;
    private LocalDateTime datetime;
    private BigDecimal totalPrice;
    List<OrderItemDTOResponse> orderItems;
    /**
     * Customize the output order information as a JSON file.
     * <p>
     * This method customizes the output order information, including
     * id, deliveryTime, note, deliveryStatus, status, datetime,
     * totalPrice and list orderItems as a JSON file.
     * @param order
     * @author HoangVu
     * @since 1.1
     */
    public OrderDTOResponse(Order order) {
        this.id = order.getId();
        this.deliveryTime = order.getDeliveryTime();
        this.note = order.getNote();
        this.deliveryStatus = order.getDeliveryStatus();
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
