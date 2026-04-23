package com.r2s.R2Sshop.rest;

import com.r2s.R2Sshop.DTO.OrderItemDTOResponse;
import com.r2s.R2Sshop.model.OrderItem;
import com.r2s.R2Sshop.service.OrderItemService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping(path = "/order_items")
public class OrderItemController extends BaseRestController{
    @Autowired
    private OrderItemService orderItemService;

    /**
     * Return orderItem by id.
     * <p>
     * This method returns orderItem by id and userName, with the id as the input parameter.
     * @param id
     * @return orderItem by id
     * @author HoangVu
     * @since 1.0
     */
    @PreAuthorize("hasRole('USER')")
    @GetMapping("/get-by-id")
    public ResponseEntity<?> getById(@AuthenticationPrincipal UserDetails userDetails,
                                         @RequestParam(name = "id", required = false) Long id) {
        OrderItem foundOrderItem = orderItemService.findById(id, userDetails.getUsername());
        return super.success(new OrderItemDTOResponse(foundOrderItem));
    }
    /**
     * Return orderItem by id for staff.
     * <p>
     * This method returns orderItem by id, with the id as the input parameter.
     * @param id
     * @return orderItem by id
     * @author HoangVu
     * @since 1.0
     */
    @PreAuthorize("hasRole('STAFF')")
    @GetMapping("/staff/get-by-id")
    public ResponseEntity<?> getByIdForStaff(@RequestParam(name = "id", required = false) Long id) {
        OrderItem foundOrderItem = orderItemService.findById(id, null);
        return super.success(new OrderItemDTOResponse(foundOrderItem));
    }
    /**
     * Return orderItem by id for admin.
     * <p>
     * This method returns orderItem by id, with the id as the input parameter.
     * @param id
     * @return orderItem by id
     * @author HoangVu
     * @since 1.0
     */
    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping("/admin/get-by-id")
    public ResponseEntity<?> getByIdForAdmin(@RequestParam(name = "id", required = false) Long id) {
        OrderItem foundOrderItem = orderItemService.findById(id, null);
        return super.success(new OrderItemDTOResponse(foundOrderItem));
    }

    /**
     * Return orderItem list by orderId.
     * <p>
     * This method returns orderItem list by orderId
     * and pagination is applied.
     * @param orderId
     * @param offset
     * @param limit
     * @return the orderItem list by orderId if the data is retrieved successfully.
     * @author HoangVu
     * @since 1.0
     */
    @PreAuthorize("hasRole('USER')")
    @GetMapping
    public ResponseEntity<?> getAllByOrderId(@AuthenticationPrincipal UserDetails userDetails,
                                             @RequestParam Long orderId,
                                             @RequestParam(defaultValue = "0") Integer offset,
                                             @RequestParam(defaultValue = "5") Integer limit) {
        Pageable pageable = PageRequest.of(offset, limit, Sort.by("id").ascending());
        Page<OrderItem> orderItemPage = orderItemService.findAllByOrderId(orderId, pageable,
                userDetails.getUsername());
        List<OrderItemDTOResponse> responses = orderItemPage.stream()
                .map(OrderItemDTOResponse:: new)
                .collect(Collectors.toList());
        return super.success(responses);
    }
    /**
     * Return orderItem list by orderId for staff.
     * <p>
     * This method returns orderItem list by orderId
     * and pagination is applied.
     * @param orderId
     * @param offset
     * @param limit
     * @return the orderItem list by orderId if the data is retrieved successfully.
     * @author HoangVu
     * @since 1.0
     */
    @PreAuthorize("hasRole('STAFF')")
    @GetMapping("/staff")
    public ResponseEntity<?> getAllByOrderIdForStaff(@RequestParam Long orderId,
                                             @RequestParam(defaultValue = "0") Integer offset,
                                             @RequestParam(defaultValue = "5") Integer limit) {
        Pageable pageable = PageRequest.of(offset, limit, Sort.by("id").ascending());
        Page<OrderItem> orderItemPage = orderItemService.findAllByOrderId(orderId, pageable,
                null);
        List<OrderItemDTOResponse> responses = orderItemPage.stream()
                .map(OrderItemDTOResponse:: new)
                .collect(Collectors.toList());
        return super.success(responses);
    }
    /**
     * Return orderItem list by orderId for admin.
     * <p>
     * This method returns orderItem list by orderId
     * and pagination is applied.
     * @param orderId
     * @param offset
     * @param limit
     * @return the orderItem list by orderId if the data is retrieved successfully.
     * @author HoangVu
     * @since 1.0
     */
    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping("/admin")
    public ResponseEntity<?> getAllByOrderIdForAdmin(@RequestParam Long orderId,
                                             @RequestParam(defaultValue = "0") Integer offset,
                                             @RequestParam(defaultValue = "5") Integer limit) {
        Pageable pageable = PageRequest.of(offset, limit, Sort.by("id").ascending());
        Page<OrderItem> orderItemPage = orderItemService.findAllByOrderId(orderId, pageable,
                null);
        List<OrderItemDTOResponse> responses = orderItemPage.stream()
                .map(OrderItemDTOResponse:: new)
                .collect(Collectors.toList());
        return super.success(responses);
    }
}
