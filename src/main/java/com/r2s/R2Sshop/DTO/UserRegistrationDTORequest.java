package com.r2s.R2Sshop.DTO;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Map;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class UserRegistrationDTORequest {
    @NotBlank(message = "First name cannot be empty !!!")
    private String firstName;
    @NotBlank(message = "Last name cannot be empty !!!")
    private String lastName;
    @NotBlank(message = "User name cannot be empty !!!")
    private String userName;
    @NotBlank(message = "Email cannot be empty !!!")
    private String email;
    @NotBlank(message = "Password cannot be empty !!!")
    private String password;
    @NotBlank(message = "Phone number cannot be empty !!!")
    private String phone;
    @NotBlank(message = "Role name cannot be empty !!!")
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
