package com.r2s.R2Sshop.DTO;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Map;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class AddressDTORequest {
    private String street;
    private String city;
    private String country;

    /**
     * Customize the input address information as a JSON file.
     * <p>
     * This function customizes the input address information, including
     * Street, city and country as a JSON file.
     * @param address
     * @author HoangVu
     * @since 1.0
     */
    public AddressDTORequest(Map<String, Object> address) {
        this.street = address.get("street").toString();
        this.city = address.get("city").toString();
        this.country = address.get("country").toString();
    }
}
