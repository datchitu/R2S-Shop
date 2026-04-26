package com.r2s.R2Sshop.rest;

import com.r2s.R2Sshop.DTO.UserVoucherDTORequest;
import com.r2s.R2Sshop.DTO.UserVoucherDTOResponse;
import com.r2s.R2Sshop.DTO.UserVoucherSimpleDTOResponse;
import com.r2s.R2Sshop.constants.ResponseCode;
import com.r2s.R2Sshop.model.UserVoucher;
import com.r2s.R2Sshop.repository.VoucherRepository;
import com.r2s.R2Sshop.service.UserVoucherService;
import jakarta.validation.Valid;
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
    private UserVoucherService userVoucherService;

    /**
     * Return userVoucher by id for admin.
     * <p>
     * This method returns userVoucher by id for admin, with the id as the input parameter.
     * @param id
     * @return userVoucher by id
     * @author HoangVu
     * @since 1.0
     */
    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping("/admin/get-by-id")
    public ResponseEntity<?> getByIdForAdmin(@RequestParam(name = "id", required = false
            , defaultValue = "1") Long id) {
        UserVoucher foundUserVoucher = userVoucherService.findById(id);
        return super.success(new UserVoucherDTOResponse(foundUserVoucher));
    }
    /**
     * Return userVoucher by id for staff.
     * <p>
     * This method returns userVoucher by id for staff, with the id as the input parameter.
     * @param id
     * @return userVoucher by id
     * @author HoangVu
     * @since 1.1
     */
    @PreAuthorize("hasRole('STAFF')")
    @GetMapping("/staff/get-by-id")
    public ResponseEntity<?> getByIdForStaff(@RequestParam(name = "id", required = false
            , defaultValue = "1") Long id) {
        UserVoucher foundUserVoucher = userVoucherService.findById(id);
        return super.success(new UserVoucherDTOResponse(foundUserVoucher));
    }
    /**
     * Return userVoucher list by status for admin.
     * <p>
     * This method returns userVoucher list by deleted for admin
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
    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping("/admin")
    public ResponseEntity<?> getAllByDeletedForAdmin(@RequestParam(defaultValue = "-1") Integer status,
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
     * Return userVoucher list by status for staff.
     * <p>
     * This method returns userVoucher list by deleted for staff
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
     * @since 1.1
     */
    @PreAuthorize("hasRole('STAFF')")
    @GetMapping("/staff")
    public ResponseEntity<?> getAllByDeletedForStaff(@RequestParam(defaultValue = "-1") Integer status,
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
     * This method returns userVoucher list by userName and deleted
     * (With the passed-in status -1, return all works;
     * with 0, return all by deleted == false works;
     * and otherwise, it's return all by deleted == true),
     * with the status as the input parameter.
     * @param status (-1, 0, 1)
     * @return the userVoucher list by userName and status if the data is retrieved successfully.
     * @throws AppException(ResponseCode.INVALID_PARAM) if status is outside the value (-1, 0, 1)
     * @author HoangVu
     * @since 1.1
     */
    @PreAuthorize("hasRole('USER')")
    @GetMapping("/my-voucher")
    public ResponseEntity<?> myVoucher(@AuthenticationPrincipal UserDetails userDetails,
                                                        @RequestParam(defaultValue = "0") Integer status) {
        if (!Arrays.asList(-1, 0, 1).contains(status)) {
            throw new AppException(ResponseCode.INVALID_PARAM);
        }
        String userName = userDetails.getUsername();
        List<UserVoucher> userVouchers = userVoucherService.findAllByUserNameAndDeleted(userName, status);
        List<UserVoucherSimpleDTOResponse> responses = userVouchers.stream()
                .map(UserVoucherSimpleDTOResponse :: new)
                .collect(Collectors.toList());
        return super.success(responses);
    }
    /**
     * Add new userVoucher for admin.
     * <p>
     * This method is used to add a new userVoucher with user and voucher for admin.
     * @param dtoRequest
     * @return information of userVoucher if the add process is successful
     * @throws ResponseCode.INVALID_VALUE if the passed-in parameter values such as
     * userId, voucherId and expireDate are missing
     * @author HoangVu
     * @since 1.2
     */
    @PreAuthorize("hasRole('ADMIN')")
    @PostMapping("/admin")
    public ResponseEntity<?> addWithUserAndVoucherForAdmin(@Valid @RequestBody UserVoucherDTORequest dtoRequest,
                                                           @RequestParam Long userId, @RequestParam Long voucherId) {
        UserVoucher insertedUserVoucher = userVoucherService.addWithUserAndVoucher(dtoRequest, userId, voucherId);
        return super.success(new UserVoucherDTOResponse(insertedUserVoucher));
    }
    /**
     * Add new userVoucher for staff.
     * <p>
     * This method is used to add a new userVoucher with user and voucher for staff.
     * @param dtoRequest
     * @return information of userVoucher if the add process is successful
     * @throws ResponseCode.INVALID_VALUE if the passed-in parameter values such as
     * userId, voucherId and expireDate are missing
     * @author HoangVu
     * @since 1.1
     */
    @PreAuthorize("hasRole('STAFF')")
    @PostMapping("/staff")
    public ResponseEntity<?> addWithUserAndVoucherForStaff(@Valid @RequestBody UserVoucherDTORequest dtoRequest,
                                                           @RequestParam Long userId, @RequestParam Long voucherId) {
        UserVoucher insertedUserVoucher = userVoucherService.addWithUserAndVoucher(dtoRequest, userId, voucherId);
        return super.success(new UserVoucherDTOResponse(insertedUserVoucher));
    }
    /**
     * Update userVoucher for admin.
     * <p>
     * This method is used to update userVoucher by id for admin.
     * @param dtoRequest
     * @return information of userVoucher if the add process is successful
     * @throws ResponseCode.INVALID_VALUE if the passed-in parameter values such as
     * expireDate is missing
     * @author HoangVu
     * @since 1.1
     */
    @PreAuthorize("hasRole('ADMIN')")
    @PutMapping("/admin")
    public ResponseEntity<?> updateByIdForAdmin(@RequestParam Long id,
                                                @Valid @RequestBody UserVoucherDTORequest dtoRequest) {
        UserVoucher updatedUserVoucher = userVoucherService.updateById(id, dtoRequest);
        return super.success(new UserVoucherDTOResponse(updatedUserVoucher));
    }
    /**
     * Update userVoucher for staff.
     * <p>
     * This method is used to update userVoucher by id for staff.
     * @param dtoRequest
     * @return information of userVoucher if the add process is successful
     * @throws ResponseCode.INVALID_VALUE if the passed-in parameter values such as
     * expireDate is missing
     * @author HoangVu
     * @since 1.2
     */
    @PreAuthorize("hasRole('STAFF')")
    @PutMapping("/staff")
    public ResponseEntity<?> updateByIdForStaff(@RequestParam Long id,
                                                @Valid @RequestBody UserVoucherDTORequest dtoRequest) {
        UserVoucher updatedUserVoucher = userVoucherService.updateById(id, dtoRequest);
        return super.success(new UserVoucherDTOResponse(updatedUserVoucher));
    }
    /**
     * Delete userVoucher for admin.
     * <p>
     * This method is used to delete userVoucher by id for admin.
     * @param id
     * @return "Deleted successfully" if it is deleted successfully.
     * @throws AppException(ResponseCode.MISSING_PARAM) if id is empty
     * @author HoangVu
     * @since 1.1
     */
    @PreAuthorize("hasRole('ADMIN')")
    @DeleteMapping("/admin")
    public ResponseEntity<?> deleteByIdForAdmin(@RequestParam Long id) {
        userVoucherService.deleteById(id);
        return super.success("Deleted successfully");
    }
    /**
     * Restore userVoucher for admin.
     * <p>
     * This method is used to restore userVoucher by id for admin.
     * @param id
     * @return "Restored successfully" if it is restored successfully.
     * @throws AppException(ResponseCode.MISSING_PARAM) if id is empty
     * @author HoangVu
     * @since 1.1
     */
    @PreAuthorize("hasRole('ADMIN')")
    @PutMapping("/admin/restore-by-id")
    public ResponseEntity<?> restoreByIdForAdmin(@RequestParam Long id) {
        userVoucherService.restoreById(id);
        return super.success("Restored successfully");
    }
    /**
     * Release userVoucher for admin.
     * <p>
     * This method is used to release userVoucher by id for admin.
     * @param id
     * @return "Released successfully" if it is released successfully.
     * @throws AppException(ResponseCode.MISSING_PARAM) if id is empty
     * @author HoangVu
     * @since 1.0
     */
    @PreAuthorize("hasRole('ADMIN')")
    @PutMapping("/admin/released-by-id")
    public ResponseEntity<?> releaseByIdForAdmin(@RequestParam Long id) {
        userVoucherService.releaseById(id);
        return super.success("Released successfully");
    }
    /**
     * Release userVoucher for staff.
     * <p>
     * This method is used to release userVoucher by id for staff.
     * @param id
     * @return "Released successfully" if it is released successfully.
     * @throws AppException(ResponseCode.MISSING_PARAM) if id is empty
     * @author HoangVu
     * @since 1.1
     */
    @PreAuthorize("hasRole('STAFF')")
    @PutMapping("/staff/released-by-id")
    public ResponseEntity<?> releaseByIdForStaff(@RequestParam Long id) {
        userVoucherService.releaseById(id);
        return super.success("Released successfully");
    }
    /**
     * Release all userVoucher by voucherId for admin.
     * <p>
     * This method is used to release all userVoucher by voucherId for admin.
     * @param voucherId
     * @return "Released successfully" if it is released successfully.
     * @throws AppException(ResponseCode.MISSING_PARAM) if id is empty
     * @author HoangVu
     * @since 1.0
     */
    @PreAuthorize("hasRole('ADMIN')")
    @PutMapping("/admin/released-all-by-voucher-id")
    public ResponseEntity<?> releaseAllByVoucherIdForAdmin(@RequestParam Long voucherId) {
        userVoucherService.releaseAllByVoucherId(voucherId);
        return super.success("Released successfully");
    }
    /**
     * Release userVoucher for staff.
     * <p>
     * This method is used to release all userVoucher by voucherId for staff.
     * @param voucherId
     * @return "Released successfully" if it is released successfully.
     * @throws AppException(ResponseCode.MISSING_PARAM) if id is empty
     * @author HoangVu
     * @since 1.0
     */
    @PreAuthorize("hasRole('STAFF')")
    @PutMapping("/staff/released-all-by-voucher-id")
    public ResponseEntity<?> releaseAllByVoucherIdForStaff(@RequestParam Long voucherId) {
        userVoucherService.releaseAllByVoucherId(voucherId);
        return super.success("Released successfully");
    }
    /**
     * Disable userVoucher for admin.
     * <p>
     * This method is used to disable userVoucher by id for admin.
     * @param id
     * @return "Disabled successfully" if it is disabled successfully.
     * @throws AppException(ResponseCode.MISSING_PARAM) if id is empty
     * @author HoangVu
     * @since 1.0
     */
    @PreAuthorize("hasRole('ADMIN')")
    @PutMapping("/admin/disable-by-id")
    public ResponseEntity<?> disableByIdForAdmin(@RequestParam Long id) {
        userVoucherService.disableById(id);
        return super.success("Disabled successfully");
    }
    /**
     * Disable all userVoucher by voucherId for admin.
     * <p>
     * This method is used to disable all userVoucher by voucherId for admin.
     * @param voucherId
     * @return "Disabled successfully" if it is disabled successfully.
     * @throws AppException(ResponseCode.MISSING_PARAM) if id is empty
     * @author HoangVu
     * @since 1.0
     */
    @PreAuthorize("hasRole('ADMIN')")
    @DeleteMapping("/admin/disable-all-by-Voucher-id")
    public ResponseEntity<?> disableAllByVoucherIdForAdmin(@RequestParam Long voucherId) {
        userVoucherService.disableAllById(voucherId);
        return super.success("Disabled successfully");
    }
}
