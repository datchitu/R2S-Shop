package com.r2s.R2Sshop.DTO;

import com.r2s.R2Sshop.model.VariantProduct;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class VariantServiceSimpleDTOResponse {
    private String name;
    private Double price;
    private String color;
    private String model_year;
    private Integer quantity;

    /**
     * Customize the output variant product information as a JSON file.
     * <p>
     * This method customizes the output variant product information, including
     * name, price, color, modelYear, quantity as a JSON file.
     * @param variantProduct
     * @author HoangVu
     * @since 1.0
     */
    public VariantServiceSimpleDTOResponse(VariantProduct variantProduct) {
        this.name = variantProduct.getName();
        this.price = variantProduct.getPrice();
        this.color = variantProduct.getColor();;
        this.model_year = variantProduct.getModelYear();;
        this.quantity = variantProduct.getQuantity();
    }
}
