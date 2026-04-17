package com.r2s.R2Sshop.rest;

import com.r2s.R2Sshop.DTO.UserVoucherDTORequest;
import com.r2s.R2Sshop.DTO.UserVoucherDTOResponse;
import com.r2s.R2Sshop.constants.ResponseCode;
import com.r2s.R2Sshop.model.UserVoucher;
import com.r2s.R2Sshop.service.UserVoucherService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
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
@RequestMapping(path = "/user_vouchers")
public class UserVoucherController extends BaseRestController{
    @Autowired
    UserVoucherService userVoucherService;

    /**
     * Return userVoucher by id.
     * <p>
     * This function returns userVoucher by id, with the id as the input parameter.
     * @param id
     * @return userVoucher by id
     * @author HoangVu
     * @since 1.0
     */
    @PreAuthorize("hasAnyRole('ADMIN', 'STAFF')")
    @GetMapping("/get-by-id")
    public ResponseEntity<?> getById(@RequestParam(name = "id", required = false
            , defaultValue = "1") Long id) {
        UserVoucher foundUserVoucher = userVoucherService.findById(id);
        return super.success(new UserVoucherDTOResponse(foundUserVoucher));
    }
    /**
     * Return userVoucher list by status.
     * <p>
     * This function returns userVoucher list by deleted
     * (With the passed-in status -1, return all works;
     * with 0, return all by deleted == false works;
     * and otherwise, it's return all by deleted == true),
     * with the status as the input parameter
     * and pagination is applied.
     * @param offset
     * @param limit
     * @param status (-1, 0, 1)
     * @return the userVoucher list by status if the data is retrieved successfully.
     * @throws AppException(ResponseCode.INVALID_PARAM) if status is outside the value (-1, 0, 1)
     * @author HoangVu
     * @since 1.0
     */
    @PreAuthorize("hasAnyRole('ADMIN', 'STAFF')")
    @GetMapping
    public ResponseEntity<?> getAllByDeleted(@RequestParam(defaultValue = "-1") Integer status,
                                             @RequestParam(defaultValue = "0") Integer offset,
                                             @RequestParam(defaultValue = "5") Integer limit) {
        if (!Arrays.asList(-1, 0, 1).contains(status)) {
            throw new AppException(ResponseCode.INVALID_PARAM);
        }
        Pageable pageable = PageRequest.of(offset, limit, Sort.by("id").ascending());
        Page<UserVoucher> userVoucherPage = userVoucherService.findAllByDeleted(status, pageable);
        List<UserVoucherDTOResponse> responses = userVoucherPage.stream()
                .map(UserVoucherDTOResponse :: new)
                .collect(Collectors.toList());
        return super.success(responses);
    }
    /**
     * Return userVoucher list by status.
     * <p>
     * This function returns userVoucher list by userName and deleted
     * (With the passed-in status -1, return all works;
     * with 0, return all by deleted == false works;
     * and otherwise, it's return all by deleted == true),
     * with the status as the input parameter.
     * @param status (-1, 0, 1)
     * @return the userVoucher list by userName and status if the data is retrieved successfully.
     * @throws AppException(ResponseCode.INVALID_PARAM) if status is outside the value (-1, 0, 1)
     * @author HoangVu
     * @since 1.0
     */
    @PreAuthorize("hasRole('USER')")
    @GetMapping("/get-by-user-name-and-deleted")
    public ResponseEntity<?> getAllByUserNameAndDeleted(@AuthenticationPrincipal UserDetails userDetails,
                                                        @RequestParam(defaultValue = "1") Integer status) {
        if (!Arrays.asList(-1, 0, 1).contains(status)) {
            throw new AppException(ResponseCode.INVALID_PARAM);
        }
        String userName = userDetails.getUsername();
        List<UserVoucher> userVouchers = userVoucherService.findAllByUserNameAndDeleted(userName, status);
        List<UserVoucherDTOResponse> responses = userVouchers.stream()
                .map(UserVoucherDTOResponse :: new)
                .collect(Collectors.toList());
        return super.success(responses);
    }
    /**
     * Add new userVoucher.
     * <p>
     * This function is used to add a new userVoucher with user and voucher.
     * @param dtoRequest
     * @return information of userVoucher if the add process is successful
     * @throws AppException(ResponseCode.NO_PARAM) if the passed-in parameter values such as
     * dtoRequest is missing
     * @throws ResponseCode.INVALID_VALUE if the passed-in parameter values such as
     * userId, voucherId and expireDate are missing
     * @author HoangVu
     * @since 1.0
     */
    @PreAuthorize("hasRole('ADMIN')")
    @PostMapping
    public ResponseEntity<?> addWithUserAndVoucher(@RequestBody UserVoucherDTORequest dtoRequest,
                                                   @RequestParam Long userId, @RequestParam Long voucherId) {
        if (ObjectUtils.isEmpty(dtoRequest)) {
            throw new AppException(ResponseCode.NO_PARAM);
        }
        UserVoucher insertedUserVoucher = userVoucherService.addWithUserAndVoucher(dtoRequest, userId, voucherId);
        return super.success(new UserVoucherDTOResponse(insertedUserVoucher));
    }
    /**
     * Update userVoucher.
     * <p>
     * This function is used to update userVoucher by id.
     * @param dtoRequest
     * @return information of userVoucher if the add process is successful
     * @throws AppException(ResponseCode.NO_PARAM) if the passed-in parameter values such as
     * dtoRequest is missing
     * @throws ResponseCode.INVALID_VALUE if the passed-in parameter values such as
     * expireDate is missing
     * @author HoangVu
     * @since 1.0
     */
    @PreAuthorize("hasAnyRole('ADMIN', 'STAFF')")
    @PutMapping
    public ResponseEntity<?> updateById(@RequestParam Long id,
                                        @RequestBody UserVoucherDTORequest dtoRequest) {
        if (ObjectUtils.isEmpty(dtoRequest)) {
            throw new AppException(ResponseCode.NO_PARAM);
        }
        UserVoucher updatedUserVoucher = userVoucherService.updateById(id, dtoRequest);
        return super.success(new UserVoucherDTOResponse(updatedUserVoucher));
    }
    /**
     * Delete userVoucher.
     * <p>
     * This function is used to delete userVoucher by id.
     * @param id
     * @return "Deleted successfully" if it is deleted successfully.
     * @throws AppException(ResponseCode.MISSING_PARAM) if id is empty
     * @author HoangVu
     * @since 1.0
     */
    @PreAuthorize("hasRole('ADMIN')")
    @DeleteMapping("/delete-by-id")
    public ResponseEntity<?> deleteById(@RequestParam Long id) {
        userVoucherService.deleteById(id);
        return super.success("Deleted successfully");
    }
    /**
     * Reactivate userVoucher.
     * <p>
     * This function is used to reactivate userVoucher by id.
     * @param id
     * @return "Reactivated successfully" if it is reactivated successfully.
     * @throws AppException(ResponseCode.MISSING_PARAM) if id is empty
     * @author HoangVu
     * @since 1.0
     */
    @PreAuthorize("hasRole('ADMIN')")
    @PutMapping("/reactivate-by-id")
    public ResponseEntity<?> reactivateById(@RequestParam Long id) {
        userVoucherService.reactivateById(id);
        return super.success("Reactivated successfully");
    }
    /**
     * Release userVoucher.
     * <p>
     * This function is used to release userVoucher by id.
     * @param id
     * @return "Released successfully" if it is released successfully.
     * @throws AppException(ResponseCode.MISSING_PARAM) if id is empty
     * @author HoangVu
     * @since 1.0
     */
    @PreAuthorize("hasAnyRole('ADMIN', 'STAFF')")
    @PutMapping("/released-by-id")
    public ResponseEntity<?> releaseById(@RequestParam Long id) {
        userVoucherService.releaseById(id);
        return super.success("Released successfully");
    }
    /**
     * Use userVoucher.
     * <p>
     * This function is used to apply userVoucher by id for cart.
     * @param id
     * @return "Applied successfully" if it is applied successfully.
     * @throws AppException(ResponseCode.MISSING_PARAM) if id is empty
     * @author HoangVu
     * @since 1.0
     */
    @PreAuthorize("hasRole('USER')")
    @PutMapping("/use-by-id")
    public ResponseEntity<?> useById(@RequestParam Long id) {
        userVoucherService.useById(id);
        return super.success("Applied successfully");
    }
    /**
     * Disable userVoucher.
     * <p>
     * This function is used to disable userVoucher by id.
     * @param id
     * @return "Disabled successfully" if it is disabled successfully.
     * @throws AppException(ResponseCode.MISSING_PARAM) if id is empty
     * @author HoangVu
     * @since 1.0
     */
    @PreAuthorize("hasRole('ADMIN')")
    @PutMapping("/disable-by-id")
    public ResponseEntity<?> disableById(@RequestParam Long id) {
        userVoucherService.disableById(id);
        return super.success("Disabled successfully");
    }
}
