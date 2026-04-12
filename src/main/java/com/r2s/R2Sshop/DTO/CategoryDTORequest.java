package com.r2s.R2Sshop.DTO;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CategoryDTORequest {
    @NotBlank(message = "Name cannot be empty !!!")
    private String name;
}
