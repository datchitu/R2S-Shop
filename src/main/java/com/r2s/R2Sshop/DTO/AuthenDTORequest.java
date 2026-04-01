package com.r2s.R2Sshop.DTO;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class AuthenDTORequest {
    private String userNane;
    private String password;
}
