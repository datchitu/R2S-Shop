package com.r2s.R2Sshop.DTO;

import com.r2s.R2Sshop.model.Product;
import lombok.AllArgsConstructor;
import lombok.Data;
import org.springframework.util.ObjectUtils;
import java.util.List;
import java.util.stream.Collectors;

@Data
@AllArgsConstructor
public class ProductDTOResponse {
    private Long id;
    private String name;
    private Boolean deleted;
    private List<VariantServiceSimpleDTOResponse> variantProducts;

    /**
     * Customize the output product information as a JSON file.
     * <p>
     * This method customizes the output product information, including
     * ID, name, deleted and list variantProducts as a JSON file.
     * @param product
     * @author HoangVu
     * @since 1.2
     */
    public ProductDTOResponse(Product product) {
        this.id = product.getId();
        this.name = product.getName();
        this.deleted = product.getDeleted();
        if (!ObjectUtils.isEmpty(product.getVariantProducts())) {
            this.variantProducts = product.getVariantProducts().stream()
                    .map(VariantServiceSimpleDTOResponse::new)
                    .collect(Collectors.toList());
        }
    }
}
