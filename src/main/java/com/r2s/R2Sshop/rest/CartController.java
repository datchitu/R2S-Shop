package com.r2s.R2Sshop.rest;

import com.r2s.R2Sshop.DTO.CartDTOResponse;
import com.r2s.R2Sshop.model.Cart;
import com.r2s.R2Sshop.service.CartService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(path = "/carts")
public class CartController extends BaseRestController{
    @Autowired
    private CartService cartService;
    /**
     * Return cart by id.
     * <p>
     * This function returns cart by id, with the id as the input parameter.
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
}
