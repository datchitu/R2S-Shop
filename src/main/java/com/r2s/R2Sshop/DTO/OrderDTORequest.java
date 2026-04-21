package com.r2s.R2Sshop.DTO;

import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.validation.constraints.Future;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class OrderDTORequest {
    @NotNull(message = "DeliveryTime cannot be empty !!!")
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss")
    @Future(message = "ExpireDate must be in the future !!!")
    private LocalDateTime deliveryTime;
    @NotNull(message = "TotalPrice cannot be empty !!!")
    @Min(value = 0, message = "TotalPrice must be greater than or equal to 0 !!!")
    private BigDecimal totalPrice;
}
