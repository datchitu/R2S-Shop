package com.r2s.R2Sshop.DTO;

import com.r2s.R2Sshop.model.User;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class UserSimpleDTOResponse {
    private String firstName;
    private String lastName;
    private String userName;
    private String email;
    private String phone;

    /**
     * Customize the output user information as a JSON file.
     * <p>
     * This function customizes the output user information, including
     * firstName, lastName, userName, email and phone as a JSON file.
     * @param user
     * @author HoangVu
     * @since 1.0
     */
    public UserSimpleDTOResponse(User user) {
        this.firstName = user.getFirstName();
        this.lastName = user.getLastName();
        this.userName = user.getUserName();
        this.email = user.getEmail();
        this.phone = user.getPhone();
    }
}
