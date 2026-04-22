package com.r2s.R2Sshop.rest;

import com.r2s.R2Sshop.DTO.OrderDTOResponse;
import com.r2s.R2Sshop.model.Order;
import com.r2s.R2Sshop.service.OrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

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
}
