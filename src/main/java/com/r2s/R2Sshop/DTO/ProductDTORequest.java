package com.r2s.R2Sshop.DTO;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Map;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ProductDTORequest {
    private String name;
    /**
     * Customize the input product information as a JSON file.
     * <p>
     * This function customizes the input product information, including name as a JSON file.
     * @param product
     * @author HoangVu
     * @since 1.0
     */
    public ProductDTORequest(Map<String, Object> product) {
        this.name = product.get("name").toString();
    }
}
