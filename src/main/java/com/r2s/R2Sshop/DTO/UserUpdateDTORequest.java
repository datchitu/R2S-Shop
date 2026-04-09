package com.r2s.R2Sshop.DTO;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Map;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class UserUpdateDTORequest {
    private String firstName;
    private String lastName;
    private String email;
    private String phone;

    /**
     * Customize the input user information as a JSON file.
     * <p>
     * This function customizes the input user information, including
     * FirstName, lastName, email and phone as a JSON file.
     * @param user
     * @author HoangVu
     * @since 1.1
     */
    public UserUpdateDTORequest(Map<String, Object> user) {
        this.firstName = user.get("firstName").toString();
        this.lastName = user.get("lastName").toString();
        this.email = user.get("email").toString();
        this.phone = user.get("phone").toString();
    }
}
