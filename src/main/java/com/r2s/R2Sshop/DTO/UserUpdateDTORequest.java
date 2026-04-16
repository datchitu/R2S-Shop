package com.r2s.R2Sshop.DTO;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class UserUpdateDTORequest {
    @NotBlank(message = "First name cannot be empty !!!")
    private String firstName;
    @NotBlank(message = "Last name cannot be empty !!!")
    private String lastName;
    @NotBlank(message = "Email cannot be empty !!!")
    private String email;
    @NotBlank(message = "Phone number cannot be empty !!!")
    private String phone;
}
