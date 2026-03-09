package com.r2s.R2Sshop.DTO;

import com.r2s.R2Sshop.model.Category;
import lombok.AllArgsConstructor;
import lombok.Data;
import org.springframework.util.ObjectUtils;

import java.util.List;
import java.util.Map;

@Data
@AllArgsConstructor
public class CategoryDTOResponse {
    private Long id;
    private String name;
    private List<Map<String, Object>> products;

    public CategoryDTOResponse(Category category) {
        this.id = category.getId();
        this.name = category.getName();

//        if(!ObjectUtils.isEmpty(category))
    }
}
