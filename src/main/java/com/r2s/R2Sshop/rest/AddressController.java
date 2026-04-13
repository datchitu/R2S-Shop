package com.r2s.R2Sshop.rest;

import com.r2s.R2Sshop.DTO.AddressDTORequest;
import com.r2s.R2Sshop.DTO.AddressDTOResponse;
import com.r2s.R2Sshop.constants.ResponseCode;
import com.r2s.R2Sshop.model.Address;
import com.r2s.R2Sshop.service.AddressService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.util.ObjectUtils;
import org.springframework.web.bind.annotation.*;

import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@RestController
@RequestMapping(path = "/addresses")
public class AddressController extends BaseRestController{
    @Autowired
    AddressService addressService;

    /**
     * Return address list by userName and status.
     * <p>
     * This function returns address list by userName and
     * status(With the passed-in status -1, return all by userName works;
     * with 0, return all by userName and deleted == false works;
     * and otherwise, it's return all by userName and deleted == true),
     * with the status as the input parameter.
     * @param status (-1, 0, 1)
     * @return the address list entity by userName and status if the data is retrieved successfully.
     * @throws AppException(ResponseCode.NO_PARAM) if status is outside the value (-1, 0, 1)
     * based on the passed-in ID parameter
     * @author HoangVu
     * @since 1.2
     */
    @RequestMapping("/get-by-user-name")
    public ResponseEntity<?> getAllByUserIdAndDeleted(@AuthenticationPrincipal UserDetails userDetails,
                                                      @RequestParam(defaultValue = "-1") Integer status) {
        if (!Arrays.asList(-1, 0, 1).contains(status)) {
            throw new AppException(ResponseCode.INVALID_PARAM);
        }
        String userName = userDetails.getUsername();
        List<Address> addresses = addressService.findByUserIdAndDeleted(status, userName);
        List<AddressDTOResponse> responses = addresses.stream()
                .map(AddressDTOResponse :: new)
                .collect(Collectors.toList());
        return super.success(responses);
    }
    /**
     * Add new address with userName.
     * <p>
     * This function is used to add a new address with userName.
     * @param dtoRequest
     * @return address information with userName if it is added successfully.
     * @throws AppException(ResponseCode.NO_PARAM) if dtoRequest is empty
     * @throws ResponseCode.INVALID_VALUE if the passed-in parameter values such as
     * street, city, country, receiverName or phoneNumber are missing
     * @author HoangVu
     * @since 1.3
     */
    @PostMapping
    public ResponseEntity<?> addWithUserName(@Valid @RequestBody AddressDTORequest dtoRequest,
                                             @AuthenticationPrincipal UserDetails userDetails) {
        if (ObjectUtils.isEmpty(dtoRequest)) {
            throw new AppException(ResponseCode.NO_PARAM);
        }
        String userName = userDetails.getUsername();
        Address insertedAddress = addressService.addWithUser(userName, dtoRequest);
        return super.success(new AddressDTOResponse(insertedAddress));
    }
    /**
     * Update address with userName.
     * <p>
     * This function is used to update address with userName.
     * @param id
     * @param dtoRequest
     * @return address information with userName if it is updated successfully.
     * @throws AppException(ResponseCode.MISSING_PARAM) if id is empty
     * @throws AppException(ResponseCode.NO_PARAM) if dtoRequest is empty
     * @throws ResponseCode.INVALID_VALUE if the passed-in parameter values such as
     * street, city, country, receiverName or phoneNumber are missing
     * @author HoangVu
     * @since 1.3
     */
    @PreAuthorize("hasRole('USER')")
    @PutMapping
    public ResponseEntity<?> updateByIdAndUserName(@RequestParam Long id,
                                 @Valid @RequestBody AddressDTORequest dtoRequest,
                                 @AuthenticationPrincipal UserDetails userDetails) {
        if (ObjectUtils.isEmpty(dtoRequest)) {
            throw new AppException(ResponseCode.NO_PARAM);
        }
        String userName = userDetails.getUsername();
        Address updatedAddress = addressService.updateByIdAndUserName(userName, id, dtoRequest);
        return super.success(new AddressDTOResponse(updatedAddress));
    }
    /**
     * Delete address with userName.
     * <p>
     * This function is used to delete address by id and userName.
     * @param id
     * @return "Deleted successfully" if it is deleted successfully.
     * @throws AppException(ResponseCode.MISSING_PARAM) if id is empty
     * @throws AppException(ResponseCode.FAILURE_ADDRESS_DELETE) if it is deleted fails
     * @author HoangVu
     * @since 1.3
     */
    @PreAuthorize("hasRole('USER')")
    @DeleteMapping("/delete-by-id-and-user-name")
    public ResponseEntity<?> deleteByIdAndUserName(@RequestParam Long id,
                                                   @AuthenticationPrincipal UserDetails userDetails) {
        String userName = userDetails.getUsername();
        addressService.deleteByIdAndUserName(userName, id);
        return super.success("Deleted successfully");
    }
    /**
     * Reactivated address.
     * <p>
     * This function is used to reactivated address by id.
     * @param id
     * @return "Reactivated successfully" if it is reactivated successfully.
     * @throws AppException(ResponseCode.MISSING_PARAM) if id is empty
     * @throws AppException(ResponseCode.FAILURE_ADDRESS_REACTIVATE) if it is reactivated fails
     * @author HoangVu
     * @since 1.2
     */
    @PreAuthorize("hasRole('ADMIN')")
    @PutMapping("/reactivate-by-id")
    public ResponseEntity<?> reactivateByIdAndUserName(@RequestParam Long id) {
        addressService.reactivateById(id);
        return super.success("Reactivated successfully");
    }
    /**
     * Set default address by id and userName.
     * <p>
     * This function Sets default address by id and userName, with the userName, id as the input parameter.
     * @param id
     * @return "Set default successfully" if it is deleted successfully.
     * @throws AppException(ResponseCode.MISSING_PARAM) if id is empty
     * @throws AppException(ResponseCode.FAILURE_ADDRESS_DELETE) if it is deleted fails
     * @author HoangVu
     * @since 1.0
     */
    @PreAuthorize("hasRole('USER')")
    @PutMapping("/set-default-by-id")
    public ResponseEntity<?> setDefaultByIdAndUserName(@RequestParam Long id,
                                                   @AuthenticationPrincipal UserDetails userDetails) {
        String userName = userDetails.getUsername();
        addressService.setDefault(userName, id);
        return super.success("Set default successfully");
    }
}
