package com.r2s.R2Sshop.DTO;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class AuthenDTOResponse {
    private String token;
    private String message;
}
