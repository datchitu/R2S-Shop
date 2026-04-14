package com.r2s.R2Sshop.DTO;

import com.r2s.R2Sshop.model.Address;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class AddressSimpleDTOResponse {
    private String street;
    private String city;
    private String country;
    private String receiverName;
    private String phoneNumber;
    private Boolean defaulted;

    /**
     * Customize the output address information as a JSON file.
     * <p>
     * This function customizes the output address information, including
     * street, city, country, defaulted, receiverName
     * and phoneNumber as a JSON file.
     * @param address
     * @author HoangVu
     * @since 1.0
     */
    public AddressSimpleDTOResponse(Address address) {
        this.street = address.getStreet();
        this.city = address.getCity();
        this.country = address.getCountry();
        this.receiverName = address.getReceiverName();
        this.phoneNumber = address.getPhoneNumber();
        this.defaulted = address.getDefaulted();
    }
}
