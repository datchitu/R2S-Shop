package com.r2s.R2Sshop.DTO;

import com.r2s.R2Sshop.model.Role;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class RoleSimpleDTOResponse {
    private String roleName;

    /**
     * Customize the output role information as a JSON file
     * <p>
     * This function customizes the output role information, including
     * roleName as a JSON file.
     * @param role
     * @author HoangVu
     * @since 1.0
     */
    public RoleSimpleDTOResponse(Role role) {
        this.roleName = role.getRoleName();
    }
}
