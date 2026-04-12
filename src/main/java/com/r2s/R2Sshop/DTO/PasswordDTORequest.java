package com.r2s.R2Sshop.DTO;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class PasswordDTORequest {
    @NotBlank(message = "Old password cannot be empty !!!")
    private String oldPassword;
    @NotBlank(message = "New password cannot be empty !!!")
    private String newPassword;
}
