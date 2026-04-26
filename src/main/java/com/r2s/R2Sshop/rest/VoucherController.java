package com.r2s.R2Sshop.rest;

import com.r2s.R2Sshop.DTO.VoucherDTORequest;
import com.r2s.R2Sshop.DTO.VoucherDTOResponse;
import com.r2s.R2Sshop.constants.ResponseCode;
import com.r2s.R2Sshop.model.Voucher;
import com.r2s.R2Sshop.service.VoucherService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.util.ObjectUtils;
import org.springframework.web.bind.annotation.*;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping(path = "/vouchers")
public class VoucherController extends BaseRestController{
    @Autowired
    private VoucherService voucherService;
    /**
     * Return voucher by id for admin.
     * <p>
     * This method returns voucher by id for admin, with the id as the input parameter.
     * @param id
     * @return voucher by id
     * @author HoangVu
     * @since 1.1
     */
    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping("/admin/get-by-id")
    public ResponseEntity<?> getByIdForAdmin(@RequestParam(name = "id", required = false
            , defaultValue = "1") Long id) {
        Voucher foundVoucher = voucherService.findById(id);
        return super.success(new VoucherDTOResponse(foundVoucher));
    }
    /**
     * Return voucher by id for staff.
     * <p>
     * This method returns voucher by id for staff, with the id as the input parameter.
     * @param id
     * @return voucher by id
     * @author HoangVu
     * @since 1.0
     */
    @PreAuthorize("hasRole('STAFF')")
    @GetMapping("/staff/get-by-id")
    public ResponseEntity<?> getByIdForStaff(@RequestParam(name = "id", required = false
            , defaultValue = "1") Long id) {
        Voucher foundVoucher = voucherService.findById(id);
        return super.success(new VoucherDTOResponse(foundVoucher));
    }
    /**
     * Return voucher list by status for admin.
     * <p>
     * This method returns voucher list by deleted for staff
     * (With the passed-in status -1, return all works;
     * with 0, return all by deleted == false works;
     * and otherwise, it's return all by deleted == true),
     * with the status as the input parameter
     * and pagination is applied.
     * @param offset
     * @param limit
     * @param status (-1, 0, 1)
     * @return the voucher list by status if the data is retrieved successfully.
     * @throws AppException(ResponseCode.INVALID_PARAM) if status is outside the value (-1, 0, 1)
     * @author HoangVu
     * @since 1.1
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
        Page<Voucher> voucherPage = voucherService.findAllByDeleted(status, pageable);
        List<VoucherDTOResponse> responses = voucherPage.stream()
                .map(VoucherDTOResponse :: new)
                .collect(Collectors.toList());
        return super.success(responses);
    }
    /**
     * Return voucher list by status for staff.
     * <p>
     * This method returns voucher list by deleted for staff
     * (With the passed-in status -1, return all works;
     * with 0, return all by deleted == false works;
     * and otherwise, it's return all by deleted == true),
     * with the status as the input parameter
     * and pagination is applied.
     * @param offset
     * @param limit
     * @param status (-1, 0, 1)
     * @return the voucher list by status if the data is retrieved successfully.
     * @throws AppException(ResponseCode.INVALID_PARAM) if status is outside the value (-1, 0, 1)
     * @author HoangVu
     * @since 1.0
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
        Page<Voucher> voucherPage = voucherService.findAllByDeleted(status, pageable);
        List<VoucherDTOResponse> responses = voucherPage.stream()
                .map(VoucherDTOResponse :: new)
                .collect(Collectors.toList());
        return super.success(responses);
    }
    /**
     * Add new voucher with category.
     * <p>
     * This method is used to add a new voucher.
     * @param dtoRequest
     * @return information of voucher if the add process is successful
     * @throws ResponseCode.INVALID_VALUE if the passed-in parameter values such as
     * name, code, discount, quantity and expireDate are missing
     * @author HoangVu
     * @since 1.2
     */
    @PreAuthorize("hasRole('ADMIN')")
    @PostMapping("/admin")
    public ResponseEntity<?> add(@Valid @RequestBody VoucherDTORequest dtoRequest) {
        Voucher insertedVoucher = voucherService.add(dtoRequest);
        return super.success(new VoucherDTOResponse(insertedVoucher));
    }
    /**
     * Update voucher for admin.
     * <p>
     * This method is used to update voucher by id for admin.
     * @param id
     * @param dtoRequest
     * @return voucher information by id if it is updated successfully.
     * @throws AppException(ResponseCode.MISSING_PARAM) if the passed-in parameter values such as
     * id is missing
     * @author HoangVu
     * @since 1.2
     */
    @PreAuthorize("hasAnyRole('ADMIN')")
    @PutMapping("/admin")
    public ResponseEntity<?> updateByIdForAdmin(@RequestParam Long id,
                                    @Valid @RequestBody VoucherDTORequest dtoRequest) {
        Voucher updatedVoucher = voucherService.updateById(id, dtoRequest);
        return super.success(new VoucherDTOResponse(updatedVoucher));
    }
    /**
     * Update voucher for staff.
     * <p>
     * This method is used to update voucher by id for staff.
     * @param id
     * @param dtoRequest
     * @return voucher information by id if it is updated successfully.
     * @throws AppException(ResponseCode.MISSING_PARAM) if the passed-in parameter values such as
     * id is missing
     * @author HoangVu
     * @since 1.1
     */
    @PreAuthorize("hasRole('STAFF')")
    @PutMapping("/staff")
    public ResponseEntity<?> updateByIdForStaff(@RequestParam Long id,
                                        @Valid @RequestBody VoucherDTORequest dtoRequest) {
        Voucher updatedVoucher = voucherService.updateById(id, dtoRequest);
        return super.success(new VoucherDTOResponse(updatedVoucher));
    }
    /**
     * Delete voucher.
     * <p>
     * This method is used to delete voucher by id.
     * @param id
     * @return "Deleted successfully" if it is deleted successfully.
     * @throws AppException(ResponseCode.MISSING_PARAM) if id is empty
     * @author HoangVu
     * @since 1.1
     */
    @PreAuthorize("hasRole('ADMIN')")
    @DeleteMapping("/admin/delete-by-id")
    public ResponseEntity<?> deleteById(@RequestParam Long id) {
        voucherService.deleteById(id);
        return super.success("Deleted successfully");
    }

    /**
     * Restore voucher.
     * <p>
     * This method is used to restore voucher by id.
     * @param id
     * @return "Restored successfully" if it is restored successfully.
     * @throws AppException(ResponseCode.MISSING_PARAM) if id is empty
     * @author HoangVu
     * @since 1.1
     */
    @PreAuthorize("hasRole('ADMIN')")
    @PutMapping("/admin/restore-by-id")
    public ResponseEntity<?> restoreById(@RequestParam Long id) {
        voucherService.restoreById(id);
        return super.success("Restored successfully");
    }
}
