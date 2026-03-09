package com.r2s.R2Sshop.DTO;

import com.r2s.R2Sshop.model.Category;
import com.r2s.R2Sshop.model.Product;
import lombok.Data;
import org.springframework.util.ObjectUtils;

@Data
public class ProductDTOResponse {
    private Long id;
    private String name;
    private String categoryName;

//    public ProductDTOResponse(Product product) {
//        this.id = product.getId();
//        this.name = product.getName();
//        if (!ObjectUtils.isEmpty(product.getCategory())) {
//            Category category = product.getCategory();
//            this.categoryName = category.getName();
//        }
//    }
}
