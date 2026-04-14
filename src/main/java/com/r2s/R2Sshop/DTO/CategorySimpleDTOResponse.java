package com.r2s.R2Sshop.DTO;

import com.r2s.R2Sshop.model.Category;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CategorySimpleDTOResponse {
    private String name;
    private Boolean deleted;

    /**
     * Customize the output category information as a JSON file.
     * <p>
     * This function customizes the output category information, including
     * name and deleted and list products as a JSON file.
     * @param category
     * @author HoangVu
     * @since 1.0
     */
    public CategorySimpleDTOResponse(Category category) {
        this.name = category.getName();
        this.deleted = category.getDeleted();
    }
}
