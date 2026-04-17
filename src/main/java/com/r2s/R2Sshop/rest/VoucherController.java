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
     * Return voucher by id.
     * <p>
     * This function returns voucher by id, with the id as the input parameter.
     * @param id
     * @return voucher by id
     * @author HoangVu
     * @since 1.0
     */
    @PreAuthorize("hasAnyRole('ADMIN', 'STAFF')")
    @GetMapping("/get-by-id")
    public ResponseEntity<?> getById(@RequestParam(name = "id", required = false
            , defaultValue = "1") Long id) {
        Voucher foundVoucher = voucherService.findById(id);
        return super.success(new VoucherDTOResponse(foundVoucher));
    }

    /**
     * Return voucher list by status.
     * <p>
     * This function returns voucher list by deleted
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
    @PreAuthorize("hasAnyRole('ADMIN', 'STAFF')")
    @GetMapping
    public ResponseEntity<?> getAllByDeleted(@RequestParam(defaultValue = "-1") Integer status,
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
     * This function is used to add a new voucher.
     * @param dtoRequest
     * @return information of voucher if the add process is successful
     * @throws AppException(ResponseCode.NO_PARAM) if the passed-in parameter values such as
     * dtoRequest is missing
     * @throws ResponseCode.INVALID_VALUE if the passed-in parameter values such as
     * name, code, discount, quantity and expireDate are missing
     * @author HoangVu
     * @since 1.0
     */
    @PreAuthorize("hasRole('ADMIN')")
    @PostMapping
    public ResponseEntity<?> add(@Valid @RequestBody VoucherDTORequest dtoRequest) {
        if (ObjectUtils.isEmpty(dtoRequest)) {
            throw new AppException(ResponseCode.NO_PARAM);
        }
        Voucher insertedVoucher = voucherService.add(dtoRequest);
        return super.success(new VoucherDTOResponse(insertedVoucher));
    }
    /**
     * Update voucher.
     * <p>
     * This function is used to update voucher by id.
     * @param id
     * @param dtoRequest
     * @return voucher information by id if it is updated successfully.
     * @throws AppException(ResponseCode.MISSING_PARAM) if the passed-in parameter values such as
     * id is missing
     * @throws AppException(ResponseCode.NO_PARAM) if dtoRequest is empty
     * @author HoangVu
     * @since 1.0
     */
    @PreAuthorize("hasAnyRole('ADMIN', 'STAFF')")
    @PutMapping
    public ResponseEntity<?> updateById(@RequestParam Long id,
                                    @Valid @RequestBody VoucherDTORequest dtoRequest) {
        if (ObjectUtils.isEmpty(dtoRequest)) {
            throw new AppException(ResponseCode.NO_PARAM);
        }
        Voucher updatedVoucher = voucherService.updateById(id, dtoRequest);
        return super.success(new VoucherDTOResponse(updatedVoucher));
    }
    /**
     * Delete voucher.
     * <p>
     * This function is used to delete voucher by id.
     * @param id
     * @return "Deleted successfully" if it is deleted successfully.
     * @throws AppException(ResponseCode.MISSING_PARAM) if id is empty
     * @author HoangVu
     * @since 1.0
     */
    @PreAuthorize("hasRole('ADMIN')")
    @DeleteMapping("/delete-by-id")
    public ResponseEntity<?> deleteById(@RequestParam Long id) {
        voucherService.deleteById(id);
        return super.success("Deleted successfully");
    }

    /**
     * Reactivate voucher.
     * <p>
     * This function is used to reactivate voucher by id.
     * @param id
     * @return "Reactivated successfully" if it is reactivated successfully.
     * @throws AppException(ResponseCode.MISSING_PARAM) if id is empty
     * @author HoangVu
     * @since 1.0
     */
    @PreAuthorize("hasRole('ADMIN')")
    @PutMapping("/reactivate-by-id")
    public ResponseEntity<?> reactivateById(@RequestParam Long id) {
        voucherService.reactivateById(id);
        return super.success("Reactivated successfully");
    }
}
