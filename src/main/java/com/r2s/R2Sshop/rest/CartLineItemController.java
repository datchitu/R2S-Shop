package com.r2s.R2Sshop.rest;

import com.r2s.R2Sshop.DTO.CartLineItemDTORequest;
import com.r2s.R2Sshop.DTO.CartLineItemDTOResponse;
import com.r2s.R2Sshop.constants.ResponseCode;
import com.r2s.R2Sshop.model.CartLineItem;
import com.r2s.R2Sshop.service.CartLineItemService;
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
import org.springframework.web.bind.annotation.*;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping(path = "/cart-line-items")
public class CartLineItemController extends BaseRestController{
    @Autowired
    private CartLineItemService cartLineItemService;

    /**
     * Return cartLineItem by id for admin.
     * <p>
     * This method returns cartLineItem by id for admin, with the id as the input parameter.
     * @param id
     * @return cart by id
     * @author HoangVu
     * @since 1.1
     */
    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping("/admin/get-by-id")
    public ResponseEntity<?> getByIdForAdmin(@RequestParam(name = "id", required = false
            , defaultValue = "1") Long id) {
        CartLineItem foundCartLineItem = cartLineItemService.findById(id);
        return super.success(new CartLineItemDTOResponse(foundCartLineItem));
    }
    /**
     * Return cartLineItem by id for staff.
     * <p>
     * This method returns cartLineItem by id for staff, with the id as the input parameter.
     * @param id
     * @return cart by id
     * @author HoangVu
     * @since 1.0
     */
    @PreAuthorize("hasRole('STAFF')")
    @GetMapping("/staff/get-by-id")
    public ResponseEntity<?> getByIdForStaff(@RequestParam(name = "id", required = false
            , defaultValue = "1") Long id) {
        CartLineItem foundCartLineItem = cartLineItemService.findById(id);
        return super.success(new CartLineItemDTOResponse(foundCartLineItem));
    }

    /**
     * Return cartLineItem list by cartId and status for admin.
     * <p>
     * This method returns cartLineItem list by cartId and deleted for admin
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
    public ResponseEntity<?> getAllByCartIdAndDeletedForAdmin(@RequestParam Long cartId,
                                                      @RequestParam(defaultValue = "-1") Integer status,
                                                      @RequestParam(defaultValue = "0") Integer offset,
                                                      @RequestParam(defaultValue = "5") Integer limit) {
        if (!Arrays.asList(-1, 0, 1).contains(status)) {
            throw new AppException(ResponseCode.INVALID_PARAM);
        }
        Pageable pageable = PageRequest.of(offset, limit, Sort.by("id").ascending());
        Page<CartLineItem> cartLineItemPage = cartLineItemService
                .findAllByCartIdAndDeleted(cartId, status, pageable);
        List<CartLineItemDTOResponse> responses = cartLineItemPage.stream()
                .map(CartLineItemDTOResponse :: new)
                .collect(Collectors.toList());
        return super.success(responses);
    }
    /**
     * Return cartLineItem list by cartId and status for staff.
     * <p>
     * This method returns cartLineItem list by cartId and deleted for staff
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
    public ResponseEntity<?> getAllByCartIdAndDeletedForStaff(@RequestParam Long cartId,
                                                      @RequestParam(defaultValue = "-1") Integer status,
                                                      @RequestParam(defaultValue = "0") Integer offset,
                                                      @RequestParam(defaultValue = "5") Integer limit) {
        if (!Arrays.asList(-1, 0, 1).contains(status)) {
            throw new AppException(ResponseCode.INVALID_PARAM);
        }
        Pageable pageable = PageRequest.of(offset, limit, Sort.by("id").ascending());
        Page<CartLineItem> cartLineItemPage = cartLineItemService
                .findAllByCartIdAndDeleted(cartId, status, pageable);
        List<CartLineItemDTOResponse> responses = cartLineItemPage.stream()
                .map(CartLineItemDTOResponse :: new)
                .collect(Collectors.toList());
        return super.success(responses);
    }
    /**
     * Return my cartLineItem list.
     * <p>
     * This method returns cartLineItem list by cartId and pagination is applied,
     * with userName, pageable as the input parameter.
     * @param userDetails
     * @param offset
     * @param limit
     * @return cartLineItem list by cartId and deleted
     * @author HoangVu
     * @since 1.0
     */
    @PreAuthorize("hasRole('USER')")
    @GetMapping("/my-cart-line-item")
    public ResponseEntity<?> myCartLineItem(@AuthenticationPrincipal UserDetails userDetails,
                                            @RequestParam(defaultValue = "0") Integer offset,
                                            @RequestParam(defaultValue = "5") Integer limit) {
        Pageable pageable = PageRequest.of(offset, limit, Sort.by("id").ascending());
        Page<CartLineItem> cartLineItemPage = cartLineItemService
                .myCartLineItem(userDetails.getUsername(), pageable);
        List<CartLineItemDTOResponse> responses = cartLineItemPage.stream()
                .map(CartLineItemDTOResponse :: new)
                .collect(Collectors.toList());
        return super.success(responses);
    }
    /**
     * Add new cartLineItem to cart with product.
     * <p>
     * This method is used to add a new cartLineItem to cart with product.
     * @param dtoRequest
     * @return information of cartLineItem if the add process is successful
     * @author HoangVu
     * @since 1.0
     */
    @PreAuthorize("hasRole('USER')")
    @PostMapping
    public ResponseEntity<?> addToCartWithProduct(@AuthenticationPrincipal UserDetails userDetails,
                                                  @RequestParam Long variantProductId,
                                                  @Valid @RequestBody CartLineItemDTORequest dtoRequest) {
        CartLineItem insertedCartLineItem = cartLineItemService
                .addToCartWithProduct(userDetails.getUsername(), variantProductId, dtoRequest);
        return super.success(new CartLineItemDTOResponse(insertedCartLineItem));
    }
    /**
     * Update cartLineItem by id.
     * <p>
     * This method updates cartLineItem by id, with the id as the input parameter.
     * @param id
     * @param dtoRequest
     * @return cartLineItem by id if the update process is successful
     * @throws AppException(ResponseCode.CART_LINE_ITEM_NOT_FOUND) if cartLineItem does not exist in the database
     * @throws AppException(ResponseCode.CART_LINE_ITEM_ALREADY_DELETED)
     * if cartLineItem already been deleted in the database
     * @author HoangVu
     * @since 1.1
     */
    @PreAuthorize("hasRole('USER')")
    @PutMapping
    public ResponseEntity<?> updateById(@RequestParam Long id,
                                        @Valid @RequestBody CartLineItemDTORequest dtoRequest) {
        CartLineItem updatedCartLineItem = cartLineItemService.updateById(id, dtoRequest);
        return super.success(new CartLineItemDTOResponse(updatedCartLineItem));
    }
    /**
     * Delete cartLineItem by id.
     * <p>
     * This method deletes cartLineItem by id, with the id as the input parameter.
     * @param id
     * @throws AppException(ResponseCode.VOUCHER_NOT_FOUND)
     * if cartLineItem does not exist in the database
     * @throws AppException(ResponseCode.CART_LINE_ITEM_ALREADY_DELETED)
     * if cartLineItem already been deleted in the database
     * @author HoangVu
     * @since 1.0
     */
    @PreAuthorize("hasRole('USER')")
    @DeleteMapping
    public ResponseEntity<?> deleteById(@RequestParam Long id) {
        cartLineItemService.deleteById(id);
        return super.success("Deleted successfully");
    }
}
