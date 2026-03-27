package com.r2s.R2Sshop.DTO;

import com.r2s.R2Sshop.model.Category;
import com.r2s.R2Sshop.model.Product;
import com.r2s.R2Sshop.model.VariantProduct;
import lombok.Data;
import org.springframework.util.ObjectUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Data
public class ProductDTOResponse {
    private Long id;
    private String name;
    private List<Map<String, Object>> variantProducts;

    /**
     * Customize the output product information as a JSON file.
     * <p>
     * This function customizes the output product information, including ID, name and list variantProducts as a JSON file.
     * @param product
     * @author HoangVu
     * @since 1.0
     */
    public ProductDTOResponse(Product product) {
        this.id = product.getId();
        this.name = product.getName();
        this.variantProducts = new ArrayList<>();
        if (!ObjectUtils.isEmpty(product.getVariantProducts())) {
            for (VariantProduct variantProduct : product.getVariantProducts()) {
                this.variantProducts.add(Map.of(
                    "variantProductId", variantProduct.getId(),
                        "variantProductName", variantProduct.getName()
                ));
            }
        }
    }
}
