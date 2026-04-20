package com.r2s.R2Sshop.DTO;

import com.r2s.R2Sshop.model.Address;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.util.ObjectUtils;

import java.util.List;
import java.util.stream.Collectors;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class AddressDTOResponse {
    private String street;
    private String city;
    private String country;
    private String receiverName;
    private String phoneNumber;
    private Boolean defaulted;
    private List<OrderSimpleDTOResponse> orders;

    /**
     * Customize the output address information as a JSON file.
     * <p>
     * This method customizes the output address information, including
     * street, city, country, order, receiverName, phoneNumber and defaulted list as a JSON file.
     * @param address
     * @author HoangVu
     * @since 1.1
     */
    public AddressDTOResponse(Address address) {
        this.street = address.getStreet();
        this.city = address.getCity();
        this.country = address.getCountry();
        this.receiverName = address.getReceiverName();
        this.phoneNumber = address.getPhoneNumber();
        this.defaulted = address.getDefaulted();
        if (!ObjectUtils.isEmpty(address.getOrders())) {
            this.orders = address.getOrders().stream()
                    .map(OrderSimpleDTOResponse :: new)
                    .collect(Collectors.toList());
        }
    }
}
