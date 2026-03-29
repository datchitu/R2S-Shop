package com.r2s.R2Sshop.DTO;

import com.r2s.R2Sshop.model.Product;
import com.r2s.R2Sshop.model.VariantProduct;
import lombok.AllArgsConstructor;
import lombok.Data;
import org.springframework.util.ObjectUtils;

@Data
@AllArgsConstructor
public class VariantProductDTOResponse {
    private Long id;
    private String name;
    private Double price;
    private String color;
    private String model_year;
    private Integer quantity;
    private Boolean deleted;

    private Long productId;
    private String productName;

    /**
     * Customize the output variant product information as a JSON file.
     * <p>
     * This function customizes the output variant product information, including
     * ID, name, price, color, model_year, quantity, deleted, productId, productName as a JSON file.
     * @param variantProduct
     * @author HoangVu
     * @since 1.1
     */
    public VariantProductDTOResponse(VariantProduct variantProduct) {
        this.id = variantProduct.getId();;
        this.name = variantProduct.getName();
        this.price = variantProduct.getPrice();
        this.color = variantProduct.getColor();;
        this.model_year = variantProduct.getModel_year();;
        this.quantity = variantProduct.getQuantity();
        this.deleted = variantProduct.getDeleted();
    }
}
