package com.r2s.R2Sshop.DTO;

import com.r2s.R2Sshop.model.Product;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
@Data
@AllArgsConstructor
@NoArgsConstructor
public class ProductSimpleDTOResponse {
    private String name;
    private Boolean deleted;

    /**
     * Customize the output product information as a JSON file.
     * <p>
     * This function customizes the output product information, including
     * name and deleted as a JSON file.
     * @param product
     * @author HoangVu
     * @since 1.0
     */
    public ProductSimpleDTOResponse(Product product) {
        this.name = product.getName();
        this.deleted = product.getDeleted();
        }
    }

