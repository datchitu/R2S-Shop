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
public class UserUpdateDTORequest {
    @NotBlank(message = "First name cannot be empty !!!")
    private String firstName;
    @NotBlank(message = "Last name cannot be empty !!!")
    private String lastName;
    @NotBlank(message = "Email cannot be empty !!!")
    private String email;
    @NotBlank(message = "Phone number cannot be empty !!!")
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
