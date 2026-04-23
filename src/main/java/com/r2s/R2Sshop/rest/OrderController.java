package com.r2s.R2Sshop.rest;

import com.r2s.R2Sshop.DTO.OrderDTORequest;
import com.r2s.R2Sshop.DTO.OrderDTOResponse;
import com.r2s.R2Sshop.constants.ResponseCode;
import com.r2s.R2Sshop.model.Order;
import com.r2s.R2Sshop.service.OrderService;
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
@RequestMapping(path = "/orders")
public class OrderController extends BaseRestController{
    @Autowired
    private OrderService orderService;

    /**
     * Return order by id.
     * <p>
     * This method returns order by id, with the id as the input parameter.
     * @param id
     * @return order by id
     * @author HoangVu
     * @since 1.0
     */
    @PreAuthorize("hasRole('USER')")
    @GetMapping("/get-by-id")
    public ResponseEntity<?> getById(@AuthenticationPrincipal UserDetails userDetails,
                                     @RequestParam(name = "id", required = false) Long id) {
        Order foundOrder = orderService.findById(id, userDetails.getUsername());
        return super.success(new OrderDTOResponse(foundOrder));
    }
    /**
     * Return order by id for staff.
     * <p>
     * This method returns order by id for staff, with the id as the input parameter.
     * @param id
     * @return order by id
     * @author HoangVu
     * @since 1.0
     */
    @PreAuthorize("hasRole('STAFF')")
    @GetMapping("/staff/get-by-id")
    public ResponseEntity<?> getByIdForStaff(@RequestParam(name = "id", required = false) Long id) {
        Order foundOrder = orderService.findById(id, null);
        return super.success(new OrderDTOResponse(foundOrder));
    }
    /**
     * Return order by id for admin.
     * <p>
     * This method returns order by id for admin, with the id as the input parameter.
     * @param id
     * @return order by id
     * @author HoangVu
     * @since 1.0
     */
    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping("/admin/get-by-id")
    public ResponseEntity<?> getByIdForAdmin(@RequestParam(name = "id", required = false) Long id) {
        Order foundOrder = orderService.findById(id, null);
        return super.success(new OrderDTOResponse(foundOrder));
    }
    /**
     * Return order list by deliveryStatus for staff.
     * <p>
     * This method returns order list by deliveryStatus for staff
     * (With the passed-in status -1, return all works;
     * with 0, return all by deliveryStatus == false works;
     * and otherwise, it's return all by deliveryStatus == true),
     * with the status as the input parameter
     * and pagination is applied.
     * @param offset
     * @param limit
     * @param status (-1, 0, 1)
     * @return the order list by status if the data is retrieved successfully.
     * @throws AppException(ResponseCode.INVALID_PARAM) if status is outside the value (-1, 0, 1)
     * @author HoangVu
     * @since 1.0
     */
    @PreAuthorize("hasRole('STAFF')")
    @GetMapping("/staff/get-all-by-delivery-status")
    public ResponseEntity<?> getAllByDeliveryStatusForStaff(@RequestParam(defaultValue = "-1") Integer status,
                                                            @RequestParam(defaultValue = "0") Integer offset,
                                                            @RequestParam(defaultValue = "10") Integer limit) {
        if (!Arrays.asList(-1, 0, 1).contains(status)) {
            throw new AppException(ResponseCode.INVALID_PARAM);
        }
        Pageable pageable = PageRequest.of(offset, limit, Sort.by("id").ascending());
        Page<Order> orderPage = orderService.findAllByDeliveryStatus(status, pageable);
        List<OrderDTOResponse> responses = orderPage.stream()
                .map(OrderDTOResponse :: new)
                .collect(Collectors.toList());
        return super.success(responses);
    }
    /**
     * Return order list by deliveryStatus for admin.
     * <p>
     * This method returns order list by deliveryStatus for admin
     * (With the passed-in status -1, return all works;
     * with 0, return all by deliveryStatus == false works;
     * and otherwise, it's return all by deliveryStatus == true),
     * with the status as the input parameter
     * and pagination is applied.
     * @param offset
     * @param limit
     * @param status (-1, 0, 1)
     * @return the order list by status if the data is retrieved successfully.
     * @throws AppException(ResponseCode.INVALID_PARAM) if status is outside the value (-1, 0, 1)
     * @author HoangVu
     * @since 1.0
     */
    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping("/admin/get-all-by-delivery-status")
    public ResponseEntity<?> getAllByDeliveryStatusForAdmin(@RequestParam(defaultValue = "-1") Integer status,
                                                            @RequestParam(defaultValue = "0") Integer offset,
                                                            @RequestParam(defaultValue = "10") Integer limit) {
        if (!Arrays.asList(-1, 0, 1).contains(status)) {
            throw new AppException(ResponseCode.INVALID_PARAM);
        }
        Pageable pageable = PageRequest.of(offset, limit, Sort.by("id").ascending());
        Page<Order> orderPage = orderService.findAllByDeliveryStatus(status, pageable);
        List<OrderDTOResponse> responses = orderPage.stream()
                .map(OrderDTOResponse :: new)
                .collect(Collectors.toList());
        return super.success(responses);
    }
    /**
     * Return order list by deliveryStatus and userName.
     * <p>
     * This method returns order list by deliveryStatus and userName
     * (With the passed-in status -1, return all works;
     * with 0, return all by deliveryStatus == false works;
     * and otherwise, it's return all by deliveryStatus == true),
     * with the status as the input parameter
     * and pagination is applied.
     * @param offset
     * @param limit
     * @param status (-1, 0, 1)
     * @return the order list by status if the data is retrieved successfully.
     * @throws AppException(ResponseCode.INVALID_PARAM) if status is outside the value (-1, 0, 1)
     * @author HoangVu
     * @since 1.0
     */
    @PreAuthorize("hasRole('USER')")
    @GetMapping("get-all-by-delivery-status")
    public ResponseEntity<?> getAllByDeliveryStatusAndUserName(@AuthenticationPrincipal UserDetails userDetails,
                                                               @RequestParam(defaultValue = "-1") Integer status,
                                                               @RequestParam(defaultValue = "0") Integer offset,
                                                               @RequestParam(defaultValue = "5") Integer limit) {
        if (!Arrays.asList(-1, 0, 1).contains(status)) {
            throw new AppException(ResponseCode.INVALID_PARAM);
        }
        Pageable pageable = PageRequest.of(offset, limit, Sort.by("id").ascending());
        String userName = userDetails.getUsername();
        Page<Order> orderPage = orderService.findAllByDeliveryStatusAndUserName(status, userName, pageable);
        List<OrderDTOResponse> responses = orderPage.stream()
                .map(OrderDTOResponse :: new)
                .collect(Collectors.toList());
        return super.success(responses);
    }

    /**
     * Return order list by status and userName.
     * <p>
     * This method returns order list by status and userName
     * (With the passed-in status -1, return all works;
     * with 0, return all by status == false works;
     * and otherwise, it's return all by status == true),
     * with the status as the input parameter
     * and pagination is applied.
     * @param offset
     * @param limit
     * @param status (-1, 0, 1)
     * @return the order list by status if the data is retrieved successfully.
     * @throws AppException(ResponseCode.INVALID_PARAM) if status is outside the value (-1, 0, 1)
     * @author HoangVu
     * @since 1.0
     */
    @PreAuthorize("hasRole('USER')")
    @GetMapping("/myOrder")
    public ResponseEntity<?> myOrder(@AuthenticationPrincipal UserDetails userDetails,
                                     @RequestParam(defaultValue = "-1") Integer status,
                                     @RequestParam(defaultValue = "0") Integer offset,
                                     @RequestParam(defaultValue = "5") Integer limit) {
        if (!Arrays.asList(-1, 0, 1).contains(status)) {
            throw new AppException(ResponseCode.INVALID_PARAM);
        }
        Pageable pageable = PageRequest.of(offset, limit, Sort.by("id").ascending());
        String userName = userDetails.getUsername();;
        Page<Order> orderPage = orderService.myOrder(status, userName, pageable);
        List<OrderDTOResponse> responses = orderPage.stream()
                .map(OrderDTOResponse :: new).collect(Collectors.toList());
        return super.success(responses);
    }
    /**
     * Update order by id and userName.
     * <p>
     * This method is used to update order by id and userName.
     * @param id
     * @param dtoRequest
     * @return order information by id if it is updated successfully.
     * @throws AppException(ResponseCode.MISSING_PARAM) if the passed-in parameter values such as
     * id is missing
     * @throws AppException(ResponseCode.NO_PARAM) if dtoRequest is empty
     * @author HoangVu
     * @since 1.0
     */
    @PreAuthorize("hasRole('USER')")
    @PutMapping
    public ResponseEntity<?> updateById(@AuthenticationPrincipal UserDetails userDetails,
                                        @RequestParam Long id, @RequestBody OrderDTORequest dtoRequest) {
        if (ObjectUtils.isEmpty(dtoRequest)) {
            throw new AppException(ResponseCode.NO_PARAM);
        }
        String userName = userDetails.getUsername();;
        Order updatedOrder = orderService.updateById(id, userName, dtoRequest);
        return super.success(new OrderDTOResponse(updatedOrder));
    }
    /**
     * Update address by id and userName.
     * <p>
     * This method is used to update address by orderId and userName.
     * @param id
     * @param addressId
     * @return "Updated address successfully" if it is charged successfully.
     * @throws AppException(ResponseCode.MISSING_PARAM) if the passed-in parameter values such as
     * id is missing
     * @author HoangVu
     * @since 1.0
     */
    @PreAuthorize("hasRole('USER')")
    @PutMapping("/charge-address")
    public ResponseEntity<?> chargeAddress(@AuthenticationPrincipal UserDetails userDetails,
                                           @RequestParam Long id, @RequestParam Long addressId) {
        String userName = userDetails.getUsername();
        orderService.chargeAddress(id, userName, addressId);
        return super.success("Updated address successfully");
    }
    /**
     * Set deliveryStatus by id for staff.
     * <p>
     * This method is used to set deliveryStatus by id for staff.
     * @param id
     * @return "StatusDelivery has been successfully set" if it is set successfully.
     * @throws AppException(ResponseCode.MISSING_PARAM) if the passed-in parameter values such as
     * id is missing
     * @author HoangVu
     * @since 1.0
     */
    @PreAuthorize("hasRole('STAFF')")
    @PutMapping("/staff/set-delivery-status-by-id")
    public ResponseEntity<?> setDeliveryStatusByIdForStaff(@RequestParam Long id) {
        orderService.setDeliveryStatus(id);
        return super.success("StatusDelivery has been successfully set");
    }
    /**
     * Set deliveryStatus by id for admin.
     * <p>
     * This method is used to set deliveryStatus by id for admin.
     * @param id
     * @return "StatusDelivery has been successfully set" if it is set successfully.
     * @throws AppException(ResponseCode.MISSING_PARAM) if the passed-in parameter values such as
     * id is missing
     * @author HoangVu
     * @since 1.0
     */
    @PreAuthorize("hasRole('ADMIN')")
    @PutMapping("/admin/set-status-delivery-by-id")
    public ResponseEntity<?> setDeliveryStatusByIdForAdmin(@RequestParam Long id) {
        orderService.setDeliveryStatus(id);
        return super.success("StatusDelivery has been successfully set");
    }
    /**
     * Cancel by id and userName.
     * <p>
     * This method is used to cancel order by id and userName.
     * @param id
     * @return "Canceled successfully" if it is canceled successfully.
     * @throws AppException(ResponseCode.MISSING_PARAM) if the passed-in parameter values such as
     * id is missing
     * @author HoangVu
     * @since 1.0
     */
    @PreAuthorize("hasRole('USER')")
    @DeleteMapping("/cancel-by-id")
    public ResponseEntity<?> cancelById(@AuthenticationPrincipal UserDetails userDetails,
                                                           @RequestParam Long id) {
        String userName = userDetails.getUsername();
        orderService.cancelById(id,userName);
        return super.success("Canceled successfully");
    }
    /**
     * Reactivate by id and userName.
     * <p>
     * This method is used to reactivate order by id and userName.
     * @param id
     * @return "Reactivated successfully" if it is reactivated successfully.
     * @throws AppException(ResponseCode.MISSING_PARAM) if the passed-in parameter values such as
     * id is missing
     * @author HoangVu
     * @since 1.0
     */
    @PreAuthorize("hasRole('USER')")
    @PutMapping("/reactive-by-id")
    public ResponseEntity<?> reactivateById(@AuthenticationPrincipal UserDetails userDetails,
                                                           @RequestParam Long id) {
        String userName = userDetails.getUsername();
        orderService.reactivateById(id,userName);
        return super.success("reactivated successfully");
    }
    /**
     * Delete by id for staff.
     * <p>
     * This method is used to delete order by id for staff.
     * @param id
     * @return "Deleted successfully" if it is deleted successfully.
     * @throws AppException(ResponseCode.MISSING_PARAM) if the passed-in parameter values such as
     * id is missing
     * @author HoangVu
     * @since 1.0
     */
    @PreAuthorize("hasRole('STAFF')")
    @DeleteMapping("/staff/delete-by-id")
    public ResponseEntity<?> deleteByIdForStaff(@RequestParam Long id) {
        orderService.deleteById(id);
        return super.success("Deleted successfully");
    }
    /**
     * Delete by id for admin.
     * <p>
     * This method is used to delete order by id for admin.
     * @param id
     * @return "Deleted successfully" if it is deleted successfully.
     * @throws AppException(ResponseCode.MISSING_PARAM) if the passed-in parameter values such as
     * id is missing
     * @author HoangVu
     * @since 1.0
     */
    @PreAuthorize("hasRole('ADMIN')")
    @DeleteMapping("/admin/delete-by-id")
    public ResponseEntity<?> deleteByIdForAdmin(@RequestParam Long id) {
        orderService.deleteById(id);
        return super.success("Deleted successfully");
    }
    /**
     * Restore by id for staff.
     * <p>
     * This method is used to restore order by id for staff.
     * @param id
     * @return "Restored successfully" if it is restored successfully.
     * @throws AppException(ResponseCode.MISSING_PARAM) if the passed-in parameter values such as
     * id is missing
     * @author HoangVu
     * @since 1.0
     */
    @PreAuthorize("hasRole('STAFF')")
    @PutMapping("/staff/restore-by-id")
    public ResponseEntity<?> restoreByIdForStaff(@RequestParam Long id) {
        orderService.restoreById(id);
        return super.success("Restored successfully");
    }
    /**
     * Restore by id for admin.
     * <p>
     * This method is used to restore order by id for admin.
     * @param id
     * @return "Restored successfully" if it is restored successfully.
     * @throws AppException(ResponseCode.MISSING_PARAM) if the passed-in parameter values such as
     * id is missing
     * @author HoangVu
     * @since 1.0
     */
    @PreAuthorize("hasRole('ADMIN')")
    @PutMapping("/admin/restore-by-id")
    public ResponseEntity<?> restoreByIdForAdmin(@RequestParam Long id) {
        orderService.restoreById(id);
        return super.success("Restored successfully");
    }
}
