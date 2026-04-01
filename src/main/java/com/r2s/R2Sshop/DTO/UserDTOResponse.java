package com.r2s.R2Sshop.DTO;

import com.r2s.R2Sshop.model.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.util.ObjectUtils;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

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
    private CartInfo cartInfo;


    @Data
    @Builder
    @AllArgsConstructor
    @NoArgsConstructor
    public static class CartInfo {
        private Double cartTotalPrice;
        private String cartNote;
        private Boolean cartPaymentType;
        private Boolean cartPaymentStatus;
        private Boolean cartStatus;
        private Timestamp cartPaidAt;
    }
    private List<Map<String, Object>> roles;
    private List<Map<String, Object>> orders;
    private List<Map<String, Object>> addresses;

    /**
     * Customize the output user information as a JSON file.
     * <p>
     * This function customizes the output user information, including
     * ID, firstName, lastName, userName, email, phone, deleted, cart, role list, order list and
     * address list as a JSON file.
     * @param user
     * @author HoangVu
     * @since 1.0
     */
    public UserDTOResponse(User user) {
        this.id = user.getId();
        this.firstName = user.getFirstName();
        this.lastName = user.getLastName();
        this.email = user.getEmail();
        this.phone = user.getPhone();
        this.deleted = user.getDeleted();
        if (!ObjectUtils.isEmpty(user.getCart())) {
            Cart cart = user.getCart();
            this.cartInfo = CartInfo.builder()
                    .cartTotalPrice(cart.getTotalPrice())
                    .cartNote(cart.getNote())
                    .cartPaymentType(cart.getPaymentType())
                    .cartPaymentStatus(cart.getPaymentStatus())
                    .cartStatus(cart.getStatus())
                    .cartPaidAt(cart.getPaidAt())
                    .build();
//            this.cartTotalPrice = cart.getTotalPrice();
//            this.cartNote = cart.getNote();
//            this.cartPaymentType = cart.getPaymentType();
//            this.cartPaymentStatus = cart.getPaymentStatus();
//            this.cartStatus = cart.getStatus();
//            this.cartPaidAt = cart.getPaidAt();
        }

        this.roles = new ArrayList<>();
        if (!ObjectUtils.isEmpty(user.getRoles())) {
            for (Role role : user.getRoles()) {
                this.roles.add(Map.of(
                        "roleName", role.getRoleName()
                ));
            }
        }
        this.orders = new ArrayList<>();
        if (!ObjectUtils.isEmpty(user.getOrders())) {
            for (Order order : user.getOrders()) {
                this.orders.add(Map.of(
                        "orderId", order.getId(),
                        "orderDeliveryTime", order.getDeliveryTime(),
                        "orderStatus", order.getStatus(),
                        "orderDatetime", order.getOrderDatetime(),
                        "orderTotalPrice", order.getTotalPrice(),
                        "orderAddressId", order.getAddress()
                ));
            }
        }
        this.addresses = new ArrayList<>();
        if (!ObjectUtils.isEmpty(user.getAddresses())) {
            for (Address address : user.getAddresses()) {
                this.addresses.add(Map.of(
                        "addressId", address.getId(),
                        "addressStreet", address.getStreet(),
                        "addressCity", address.getCity(),
                        "addressCountry", address.getCountry()
                ));
            }
        }
    }
}
