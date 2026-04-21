package com.r2s.R2Sshop.DTO;

import com.r2s.R2Sshop.model.OrderItem;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class OrderItemDTOResponse {
    private Long variantProductId;
    private String name;
    private BigDecimal price;
    private String color;
    private String modelYear;
    private Integer quantity;
    /**
     * Customize the output orderItem information as a JSON file.
     * <p>
     * This method customizes the output orderItem information, including
     * variantProductId, name, price, color, modelYear and quantity as a JSON file.
     * @param orderItem
     * @author HoangVu
     * @since 1.0
     */
    public OrderItemDTOResponse(OrderItem orderItem) {
        this.variantProductId = orderItem.getVariantProductId();
        this.name = orderItem.getName();
        this.price = orderItem.getPrice();
        this.color = orderItem.getColor();
        this.modelYear = orderItem.getModelYear();
        this.quantity = orderItem.getQuantity();
    }
}
