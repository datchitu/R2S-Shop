package com.r2s.R2Sshop.DTO;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class AddressDTORequest {
    @NotBlank(message = "Street cannot be empty !!!")
    private String street;
    @NotBlank(message = "City cannot be empty !!!")
    private String city;
    @NotBlank(message = "Country cannot be empty !!!")
    private String country;
    @NotBlank(message = "Receiver name cannot be empty !!!")
    private String receiverName;
    @NotBlank(message = "phone number cannot be empty !!!")
    private String phoneNumber;
}
