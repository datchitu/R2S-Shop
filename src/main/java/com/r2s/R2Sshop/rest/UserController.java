package com.r2s.R2Sshop.rest;

import com.r2s.R2Sshop.DTO.CartDTOResponse;
import com.r2s.R2Sshop.DTO.UserDTOResponse;
import com.r2s.R2Sshop.constants.ResponseCode;
import com.r2s.R2Sshop.model.Cart;
import com.r2s.R2Sshop.model.User;
import com.r2s.R2Sshop.repository.CartRepository;
import com.r2s.R2Sshop.repository.UserRepository;
import com.r2s.R2Sshop.service.CartService;
import com.r2s.R2Sshop.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.util.ObjectUtils;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping(path = "/users")
public class UserController extends BaseRestController{
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private UserService userService;
    @Autowired
    private CartRepository cartRepository;
    @Autowired
    private CartService cartService;

    /**
     * Add new user with new cart.
     * <p>
     * This function is used to add a new user with new card for this user.
     * @param newUser
     * @return user with cart information if user with cart is added successfully.
     * @throws AppException(ResponseCode.NO_PARAM) if newUser is empty
     * @throws AppException(ResponseCode.MISSING_PARAM) if the passed-in parameter values such as
     * last name, first name, phone number, email, password or userRole are missing
     * @throws AppException(ResponseCode.DATA_ALREADY_EXISTS) if user be found by userName
     * @author HoangVu
     * @since 1.1
     */

    @PostMapping("")
    public ResponseEntity<?> addUserWithCart(@RequestBody Map<String, Object> newUser) {
        if  (ObjectUtils.isEmpty(newUser)) {
            throw new AppException(ResponseCode.NO_PARAM);
        }
        if (ObjectUtils.isEmpty(newUser.get("firstName"))
                || ObjectUtils.isEmpty(newUser.get("lastName"))
                || ObjectUtils.isEmpty(newUser.get("userName"))
                || ObjectUtils.isEmpty(newUser.get("email"))
                || ObjectUtils.isEmpty(newUser.get("password"))
                || ObjectUtils.isEmpty(newUser.get("phone"))
                || ObjectUtils.isEmpty(newUser.get("userRole"))) {
            throw new AppException(ResponseCode.MISSING_PARAM);
        }
        if (userService.existsByUserName(newUser.get("userName").toString())) {
            throw new AppException(ResponseCode.DATA_ALREADY_EXISTS);
        }
        Map<String, Object> data = userService.registerUserWithCart(newUser);
        Map<String, Object> responseData = new HashMap<>();
        responseData.put("user", new UserDTOResponse((User) data.get("user")));
        responseData.put("cart", new CartDTOResponse((Cart) data.get("cart")));
        return super.success(responseData);
    }
}
