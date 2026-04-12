package com.r2s.R2Sshop.DTO;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Map;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ProductDTORequest {
    @NotBlank(message = "Name cannot be empty !!!")
    private String name;
}
