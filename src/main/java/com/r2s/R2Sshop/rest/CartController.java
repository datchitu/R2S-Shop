package com.r2s.R2Sshop.rest;

import com.r2s.R2Sshop.DTO.CartDTOResponse;
import com.r2s.R2Sshop.DTO.MyCartDTOResponse;
import com.r2s.R2Sshop.DTO.OrderDTORequest;
import com.r2s.R2Sshop.constants.ResponseCode;
import com.r2s.R2Sshop.model.Cart;
import com.r2s.R2Sshop.service.CartService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping(path = "/carts")
public class CartController extends BaseRestController{
    @Autowired
    private CartService cartService;
    /**
     * Return cart by id.
     * <p>
     * This method returns cart by id, with the id as the input parameter.
     * @param id
     * @return cart by id
     * @author HoangVu
     * @since 1.0
     */
    @PreAuthorize("hasAnyRole('ADMIN', 'STAFF')")
    @GetMapping("/get-by-id")
    public ResponseEntity<?> getById(@RequestParam(name = "id", required = false
            , defaultValue = "1") Long id) {
        Cart foundCart = cartService.findById(id);
        return super.success(new CartDTOResponse(foundCart));
    }
    /**
     * Return myCart by userName.
     * <p>
     * This method returns myCart by userName, with the userName as the input parameter.
     * @param userDetails
     * @return myCart by userName
     * @author HoangVu
     * @since 1.0
     */
    @PreAuthorize("hasRole('USER')")
    @GetMapping("/get-my-card")
    public ResponseEntity<?> myCart(@AuthenticationPrincipal UserDetails userDetails) {
        Cart foundCart = cartService.myCart(userDetails.getUsername());
        return super.success(new MyCartDTOResponse(foundCart));
    }
    /**
     * Update total price.
     * <p>
     * This method is used to update total price by id.
     * @param id
     * @return "Updated successfully" if it is deleted successfully.
     * @throws AppException(ResponseCode.MISSING_PARAM) if id is empty
     * @author HoangVu
     * @since 1.0
     */
    @PutMapping("/update-total-price")
    public ResponseEntity<?> updateTotalPrice(@RequestParam Long id) {
        cartService.updateTotalPrice(id);
        return super.success("Updated successfully");
    }
    /**
     * Payment by card.
     * <p>
     * This method is used for payment by card and apply voucher (if available) by id.
     * @param dtoRequest
     * @param userVoucherId
     * @param addressId
     * @return "Payment successful" if the payment method í successful.
     * @throws AppException(ResponseCode.MISSING_PARAM) if id and userVoucherId are empty
     * @author HoangVu
     * @since 1.1
     */
    @PreAuthorize("hasRole('USER')")
    @PutMapping("/payment-by-card")
    public ResponseEntity<?> paymentByCard(@AuthenticationPrincipal UserDetails userDetails,
                                           @Valid @RequestBody OrderDTORequest dtoRequest,
                                           @RequestParam Long userVoucherId,
                                           @RequestParam Long addressId) {
        cartService.paymentByCard(userDetails.getUsername(), userVoucherId,
                dtoRequest, addressId);
        return super.success("Payment successful");
    }
    /**
     * Payment by cash.
     * <p>
     * This method is used for payment by cash and apply voucher (if available) by id.
     * @param dtoRequest 
     * @param userVoucherId
     * @param addressId
     * @return "Payment successful" if the payment method í successful.
     * @throws AppException(ResponseCode.MISSING_PARAM) if id and userVoucherId are empty
     * @author HoangVu
     * @since 1.2
     */
    @PreAuthorize("hasRole('USER')")
    @PutMapping("/payment-by-cash")
    public ResponseEntity<?> paymentByCash(@AuthenticationPrincipal UserDetails userDetails,
                                           @Valid @RequestBody OrderDTORequest dtoRequest,
                                           @RequestParam Long userVoucherId,
                                           @RequestParam Long addressId) {
        cartService.paymentByCash(userDetails.getUsername(), userVoucherId,
                dtoRequest, addressId);
        return super.success("Payment successful");
    }
    /**
     * Set status.
     * <p>
     * This method is used to confirm card by id.
     * @param id
     * @return "Confirmed successfully" if it is confirmed successfully.
     * @throws AppException(ResponseCode.MISSING_PARAM) if id is empty
     * @author HoangVu
     * @since 1.0
     */
    @PreAuthorize("hasAnyRole('ADMIN', 'STAFF')")
    @PutMapping("/set-status")
    public ResponseEntity<?> setStatus(@RequestParam Long id) {
        cartService.setStatus(id);
        return super.success("Confirmed successfully");
    }
}
