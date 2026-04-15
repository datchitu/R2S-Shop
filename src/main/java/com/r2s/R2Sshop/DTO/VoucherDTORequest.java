package com.r2s.R2Sshop.DTO;

import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.validation.constraints.Future;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class VoucherDTORequest {
    @NotBlank(message = "Name cannot be empty !!!")
    private String name;
    @NotBlank(message = "Code cannot be empty !!!")
    private String code;
    @NotNull(message = "Discount cannot be empty !!!")
    @Min(value = 0, message = "Discount must be greater than or equal to 0 !!!")
    private Integer discount;
    @NotNull(message = "Quantity cannot be empty !!!")
    @Min(value = 1, message = "Quantity must be at least 1 !!!")
    private Integer quantity;
    @NotNull(message = "ExpireDate cannot be empty !!!")
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss")
    @Future(message = "ExpireDate must be in the future !!!")
    private LocalDateTime expireDate;
}
