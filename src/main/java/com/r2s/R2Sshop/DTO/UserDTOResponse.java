package com.r2s.R2Sshop.DTO;

import com.r2s.R2Sshop.model.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.util.ObjectUtils;

import java.util.List;
import java.util.stream.Collectors;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class UserDTOResponse {
    private Long id;
    private String firstName;
    private String lastName;
    private String userName;
    private String email;
    private String phone;
    private Boolean deleted;

    private List<CartSimpleDTOResponse> carts;
    private List<RoleSimpleDTOResponse> roles;
    private List<OrderSimpleDTOResponse> orders;
    private List<AddressSimpleDTOResponse> addresses;

    /**
     * Customize the output user information as a JSON file.
     * <p>
     * This function customizes the output user information, including
     * ID, firstName, lastName, userName, email, phone, deleted, cart list, role list, order list and
     * address list as a JSON file.
     * @param user
     * @author HoangVu
     * @since 1.2
     */
    public UserDTOResponse(User user) {
        this.id = user.getId();
        this.firstName = user.getFirstName();
        this.lastName = user.getLastName();
        this.userName = user.getUserName();
        this.email = user.getEmail();
        this.phone = user.getPhone();
        this.deleted = user.getDeleted();
        if (!ObjectUtils.isEmpty(user.getCarts())) {
                this.carts = user.getCarts().stream()
                        .map(CartSimpleDTOResponse::new)
                        .collect(Collectors.toList());
        }
        if (!ObjectUtils.isEmpty(user.getRoles())) {
            this.roles = user.getRoles().stream()
                    .map(RoleSimpleDTOResponse::new)
                    .collect(Collectors.toList());
        }
        if (!ObjectUtils.isEmpty(user.getOrders())) {
            this.orders = user.getOrders().stream()
                    .map(OrderSimpleDTOResponse::new)
                    .collect(Collectors.toList());
        }
        if (!ObjectUtils.isEmpty(user.getAddresses())) {
            this.addresses = user.getAddresses().stream()
                    .map(AddressSimpleDTOResponse::new)
                    .collect(Collectors.toList());
        }
    }
}
