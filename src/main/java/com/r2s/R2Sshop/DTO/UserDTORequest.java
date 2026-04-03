package com.r2s.R2Sshop.DTO;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Map;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class UserDTORequest {
    private String firstName;
    private String lastName;
    private String email;
    private String phone;

    public UserDTORequest(Map<String, Object> user) {
        this.firstName = user.get("firstName").toString();
        this.lastName = user.get("lastName").toString();
        this.email = user.get("email").toString();
        this.phone = user.get("phone").toString();
    }
}
