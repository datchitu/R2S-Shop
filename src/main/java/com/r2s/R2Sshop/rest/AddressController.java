package com.r2s.R2Sshop.rest;

import com.r2s.R2Sshop.DTO.AddressDTORequest;
import com.r2s.R2Sshop.DTO.AddressDTOResponse;
import com.r2s.R2Sshop.constants.ResponseCode;
import com.r2s.R2Sshop.model.Address;
import com.r2s.R2Sshop.service.AddressService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.util.ObjectUtils;
import org.springframework.web.bind.annotation.*;

import java.util.Arrays;
import java.util.List;
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
     * @throws AppException(ResponseCode.MISSING_PARAM) if the passed-in parameter values such as
     * street, city or country are missing
     * @throws AppException(ResponseCode.INSERT_FAILURE) if it is added fails
     * @author HoangVu
     * @since 1.1
     */
    @PostMapping
    public ResponseEntity<?> addWithUserName(@RequestBody AddressDTORequest dtoRequest,
                                             @AuthenticationPrincipal UserDetails userDetails) {
        if (ObjectUtils.isEmpty(dtoRequest)) {
            throw new AppException(ResponseCode.NO_PARAM);
        }
        if (ObjectUtils.isEmpty(dtoRequest.getStreet())
                || ObjectUtils.isEmpty(dtoRequest.getCity())
                || ObjectUtils.isEmpty(dtoRequest.getCountry())) {
            throw new AppException(ResponseCode.MISSING_PARAM);
        }
        String userName = userDetails.getUsername();
        Address insertedAddress = addressService.addWithUser(userName, dtoRequest);
        if (ObjectUtils.isEmpty(insertedAddress)) {
            throw new AppException(ResponseCode.INSERT_FAILURE);
        }
        return super.success(new AddressDTOResponse(insertedAddress));
    }
    /**
     * Update address with userName.
     * <p>
     * This function is used to update address with userName.
     * @param id
     * @param dtoRequest
     * @return address information with userName if it is updated successfully.
     * @throws AppException(ResponseCode.NO_PARAM) if dtoRequest or id is empty
     * @throws AppException(ResponseCode.MISSING_PARAM) if the passed-in parameter values such as
     * street, city or country are missing
     * @throws AppException(ResponseCode.FAILURE_ADDRESS_UPDATE) if it is updated fails
     * @author HoangVu
     * @since 1.1
     */
    @PutMapping
    public ResponseEntity<?> updateByIdAndUserName(@RequestParam Long id,
                                 @RequestBody AddressDTORequest dtoRequest,
                                 @AuthenticationPrincipal UserDetails userDetails) {
        if (ObjectUtils.isEmpty(dtoRequest)) {
            throw new AppException(ResponseCode.NO_PARAM);
        }
        if (ObjectUtils.isEmpty(dtoRequest.getStreet())
                || ObjectUtils.isEmpty(dtoRequest.getCity())
                || ObjectUtils.isEmpty(dtoRequest.getCountry())) {
            throw new AppException(ResponseCode.MISSING_PARAM);
        }
        String userName = userDetails.getUsername();
        Address updatedAddress = addressService.updateByIdAndUserName(userName, id, dtoRequest);
        if (ObjectUtils.isEmpty(updatedAddress)) {
            throw new AppException(ResponseCode.FAILURE_ADDRESS_UPDATE);
        }
        return super.success(new AddressDTOResponse(updatedAddress));
    }
    /**
     * Delete address with userName.
     * <p>
     * This function is used to delete address by id and userName.
     * @param id
     * @return address information with id and userName if it is deleted successfully.
     * @throws AppException(ResponseCode.NO_PARAM) if id is empty
     * @throws AppException(ResponseCode.FAILURE_ADDRESS_DELETE) if it is deleted fails
     * @author HoangVu
     * @since 1.2
     */
    @DeleteMapping("/delete-by-id-and-user-name")
    public ResponseEntity<?> deleteByIdAndUserName(@RequestParam Long id,
                                                   @AuthenticationPrincipal UserDetails userDetails) {
        String userName = userDetails.getUsername();
        Address deletedAddress = addressService.deleteByIdAndUserName(userName, id);
        if (ObjectUtils.isEmpty(deletedAddress)) {
            throw new AppException(ResponseCode.FAILURE_ADDRESS_DELETE);
        }
        return super.success("Deleted successfully");
    }
    /**
     * Reactivated address.
     * <p>
     * This function is used to reactivated address by id.
     * @param id
     * @return address information with id and userName if it is reactivated successfully.
     * @throws AppException(ResponseCode.NO_PARAM) if id is empty
     * @throws AppException(ResponseCode.FAILURE_ADDRESS_REACTIVATE) if it is reactivated fails
     * @author HoangVu
     * @since 1.1
     */
    @PreAuthorize("hasRole('ADMIN')")
    @PutMapping("/reactivate-by-id")
    public ResponseEntity<?> reactivateByIdAndUserName(@RequestParam Long id) {
        Address reactivateAddress = addressService.reactivateById(id);
        if (ObjectUtils.isEmpty(reactivateAddress)) {
            throw new AppException(ResponseCode.FAILURE_ADDRESS_REACTIVATE);
        }
        return super.success("Reactivated successfully");
    }
}
