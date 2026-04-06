package com.r2s.R2Sshop.rest;

import com.r2s.R2Sshop.DTO.AddressDTOResponse;
import com.r2s.R2Sshop.constants.ResponseCode;
import com.r2s.R2Sshop.model.Address;
import com.r2s.R2Sshop.service.AddressService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping(path = "/addresses")
public class AddressController extends BaseRestController{
    @Autowired
    AddressService addressService;

    /**
     * Return address list by userName and deleted(status).
     * <p>
     * This function returns address list by userName and
     * deleted(With the passed-in status -1, return all by productId works;
     * with 0, return all by productId and deleted == false works;
     * and otherwise, it's return all by product id and deleted == true),
     * with the status as the input parameter.
     * @param status (-1, 0, 1)
     * @return the address list entity by userName and status if the data is retrieved successfully.
     * @throws AppException(ResponseCode.NO_PARAM) if status is outside the value (-1, 0, 1)
     * based on the passed-in ID parameter
     * @author HoangVu
     * @since 1.0
     */
    @RequestMapping("/get-by-user-name")
    public ResponseEntity<?> getAllByUserIdAndDeleted(@AuthenticationPrincipal UserDetails userDetails,
                                                      @RequestParam(defaultValue = "-1") Integer status) {
        if (!Arrays.asList(-1, 0, 1).contains(status)) {
            throw new AppException(ResponseCode.INVALID_PARAM);
        }
        String userName = userDetails.getUsername();
        List<Address> addresses;
        if (status == -1) {
            addresses = this.addressService.findByUserIdAndDeleted(userName, null);
        } else if (status == 0) {
            addresses = this.addressService.findByUserIdAndDeleted(userName, false);
        } else {
            addresses = this.addressService.findByUserIdAndDeleted(userName, true);
        }
        List<AddressDTOResponse> responses = addresses.stream()
                .map(AddressDTOResponse :: new)
                .collect(Collectors.toList());
        return super.success(responses);
    }
}
