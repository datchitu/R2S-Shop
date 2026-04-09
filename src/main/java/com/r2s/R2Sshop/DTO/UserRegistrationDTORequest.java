package com.r2s.R2Sshop.DTO;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Map;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class UserRegistrationDTORequest {
    private String firstName;
    private String lastName;
    private String userName;
    private String email;
    private String password;
    private String phone;
    private String userRole;

    /**
     * Customize the input user information as a JSON file.
     * <p>
     * This function customizes the input user information, including
     * FirstName, lastName, userName, email, password, phone and userRole as a JSON file.
     * @param user
     * @author HoangVu
     * @since 1.0
     */
    public UserRegistrationDTORequest(Map<String, Object> user) {
        this.firstName = user.get("firstName").toString();
        this.lastName = user.get("lastName").toString();
        this.userName = user.get("userName").toString();
        this.email = user.get("email").toString();
        this.password = user.get("password").toString();
        this.phone = user.get("phone").toString();
        this.userRole = user.get("userRole").toString();
    }
}
