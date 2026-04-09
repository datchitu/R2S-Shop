package com.r2s.R2Sshop.DTO;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Map;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CategoryDTORequest {
    private String name;

    /**
     * Customize the input category information as a JSON file.
     * <p>
     * This function customizes the input category information, including name as a JSON file.
     * @param category
     * @author HoangVu
     * @since 1.0
     */
    public CategoryDTORequest(Map<String, Object> category) {
        this.name = category.get("name").toString();
    }
}
