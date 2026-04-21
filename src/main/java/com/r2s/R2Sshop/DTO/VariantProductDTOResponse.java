package com.r2s.R2Sshop.DTO;

import com.r2s.R2Sshop.model.VariantProduct;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class VariantProductDTOResponse {
    private Long id;
    private String name;
    private BigDecimal price;
    private String color;
    private String model_year;
    private Integer quantity;
    private Boolean deleted;

    /**
     * Customize the output variant product information as a JSON file.
     * <p>
     * This method customizes the output variant product information, including
     * ID, name, price, color, modelYear, quantity, deleted, productId, productName as a JSON file.
     * @param variantProduct
     * @author HoangVu
     * @since 1.2
     */
    public VariantProductDTOResponse(VariantProduct variantProduct) {
        this.id = variantProduct.getId();;
        this.name = variantProduct.getName();
        this.price = variantProduct.getPrice();
        this.color = variantProduct.getColor();;
        this.model_year = variantProduct.getModelYear();;
        this.quantity = variantProduct.getQuantity();
        this.deleted = variantProduct.getDeleted();
    }
}
