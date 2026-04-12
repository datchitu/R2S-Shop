package com.r2s.R2Sshop.DTO;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class AuthenDTORequest {
    @NotBlank(message = "User name cannot be empty !!!")
    private String userName;
    @NotBlank(message = "Password cannot be empty !!!")
    private String password;
}
