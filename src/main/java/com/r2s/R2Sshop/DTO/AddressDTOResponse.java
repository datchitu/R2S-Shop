package com.r2s.R2Sshop.DTO;

import com.r2s.R2Sshop.model.Address;
import com.r2s.R2Sshop.model.Order;
import lombok.AllArgsConstructor;
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
public class AddressDTOResponse {
    private String street;
    private String city;
    private String country;
    private String receiverName;
    private String phoneNumber;
    private Boolean defaulted;
    private List<Map<String, Object>> orders;

    /**
     * Customize the output address information as a JSON file.
     * <p>
     * This function customizes the output address information, including
     * street, city, country, order, receiverName, phoneNumber and defaulted list as a JSON file.
     * @param address
     * @author HoangVu
     * @since 1.0
     */
    public AddressDTOResponse(Address address) {
        this.street = address.getStreet();
        this.city = address.getCity();
        this.country = address.getCountry();
        this.receiverName = address.getReceiverName();
        this.phoneNumber = address.getPhoneNumber();
        this.defaulted = address.getDefaulted();
        this.orders = new ArrayList<>();
        if (!ObjectUtils.isEmpty(address.getOrders())) {
            for (Order order : address.getOrders()) {
                this.orders.add(Map.of(
                        "orderId", order.getId(),
                        "orderDeliveryTime", order.getDeliveryTime(),
                        "orderStatus", order.getStatus(),
                        "orderDatetime", order.getOrderDatetime(),
                        "orderTotalPrice", order.getTotalPrice()
                ));
            }
        }
    }
}
