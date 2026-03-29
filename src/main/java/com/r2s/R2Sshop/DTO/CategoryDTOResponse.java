package com.r2s.R2Sshop.DTO;

import com.r2s.R2Sshop.model.Category;
import com.r2s.R2Sshop.model.Product;
import lombok.AllArgsConstructor;
import lombok.Data;
import org.springframework.util.ObjectUtils;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Data
@AllArgsConstructor
public class CategoryDTOResponse {
    private Long id;
    private String name;
    private Boolean deleted;
    private List<Map<String, Object>> products;
    /**
     * Customize the output category information as a JSON file.
     * <p>
     * This function customizes the output category information, including
     * ID, name, deleted and list products as a JSON file.
     * @param category
     * @author HoangVu
     * @since 1.1
     */
    public CategoryDTOResponse(Category category) {
        this.id = category.getId();
        this.name = category.getName();
        this.deleted = category.getDeleted();
        this.products = new ArrayList<>();
        if(!ObjectUtils.isEmpty(category.getProducts())) {
            for (Product product : category.getProducts()) {
                this.products.add(Map.of("productId", product.getId(), "productName", product.getName()));
            }
        }
    }
}
