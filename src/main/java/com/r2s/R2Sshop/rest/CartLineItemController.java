package com.r2s.R2Sshop.rest;

import com.r2s.R2Sshop.DTO.CartLineItemDTORequest;
import com.r2s.R2Sshop.DTO.CartLineItemDTOResponse;
import com.r2s.R2Sshop.DTO.CartLineItemSimpleDTOResponse;
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
import org.springframework.util.ObjectUtils;
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
     * Return cartLineItem by id.
     * <p>
     * This function returns cartLineItem by id, with the id as the input parameter.
     * @param id
     * @return cart by id
     * @author HoangVu
     * @since 1.0
     */
    @PreAuthorize("hasAnyRole('ADMIN', 'STAFF')")
    @GetMapping("/get-by-id")
    public ResponseEntity<?> getById(@RequestParam(name = "id", required = false
            , defaultValue = "1") Long id) {
        CartLineItem foundCartLineItem = cartLineItemService.findById(id);
        return super.success(new CartLineItemDTOResponse(foundCartLineItem));
    }

    /**
     * Return cartLineItem list by cartId and status.
     * <p>
     * This function returns cartLineItem list by cartId and deleted
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
    @PreAuthorize("hasRole('USER')")
    @GetMapping
    public ResponseEntity<?> getAllByCartIdAndDeleted(@RequestParam Long cartId,
                                                      @RequestParam(defaultValue = "-1") Integer status,
                                                      @RequestParam(defaultValue = "0") Integer offset,
                                                      @RequestParam(defaultValue = "5") Integer limit) {
        if (!Arrays.asList(-1, 0, 1).contains(status)) {
            throw new AppException(ResponseCode.INVALID_PARAM);
        }
        Pageable pageable = PageRequest.of(offset, limit, Sort.by("id").ascending());
        Page<CartLineItem> cartLineItemPage = cartLineItemService
                .findAllByCartIdAndDeleted(cartId, status, pageable);
        List<CartLineItemSimpleDTOResponse> responses = cartLineItemPage.stream()
                .map(CartLineItemSimpleDTOResponse :: new)
                .collect(Collectors.toList());
        return super.success(responses);
    }
    /**
     * Add new cartLineItem to cart with product.
     * <p>
     * This function is used to add a new cartLineItem to cart with product.
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
     * This function updates cartLineItem by id, with the id as the input parameter.
     * @param id
     * @param dtoRequest
     * @return cartLineItem by id if the update process is successful
     * @throws AppException(ResponseCode.CART_LINE_ITEM_NOT_FOUND) if cartLineItem does not exist in the database
     * @throws AppException(ResponseCode.CART_LINE_ITEM_ALREADY_DELETED)
     * if cartLineItem already been deleted in the database
     * @author HoangVu
     * @since 1.0
     */
    public ResponseEntity<?> updateById(@RequestParam Long id,
                                        @RequestBody CartLineItemDTORequest dtoRequest) {
        CartLineItem updatedCartLineItem = cartLineItemService.updateById(id, dtoRequest);
        return super.success(new CartLineItemDTOResponse(updatedCartLineItem));
    }
    /**
     * Delete cartLineItem by id.
     * <p>
     * This function deletes cartLineItem by id, with the id as the input parameter.
     * @param id
     * @throws AppException(ResponseCode.VOUCHER_NOT_FOUND)
     * if cartLineItem does not exist in the database
     * @throws AppException(ResponseCode.CART_LINE_ITEM_ALREADY_DELETED)
     * if cartLineItem already been deleted in the database
     * @author HoangVu
     * @since 1.0
     */
    @PreAuthorize("hasRole('USER')")
    @DeleteMapping("/delete-by-id")
    public ResponseEntity<?> deleteById(@RequestParam Long id) {
        cartLineItemService.deleteById(id);
        return super.success("Deleted successfully");
    }
}
