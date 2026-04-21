package com.r2s.R2Sshop.DTO;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class OrderItemDTORequest {
    @NotNull(message = "VariantProductId cannot be empty !!!")
    private Long variantProductId;
    @NotBlank(message = "Name cannot be empty !!!")
    private String name;
    @NotNull(message = "Price cannot be empty !!!")
    @Min(value = 0, message = "Price must be greater than or equal to 0 !!!")
    private BigDecimal price;
    @NotBlank(message = "Color cannot be empty !!!")
    private String color;
    @NotBlank(message = "ModelYear cannot be empty !!!")
    private String modelYear;
    @NotNull(message = "Quantity cannot be empty !!!")
    @Min(value = 1, message = "Quantity must be at least 1 !!!")
    private Integer quantity;
}
