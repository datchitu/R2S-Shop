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

import javax.print.attribute.standard.JobKOctets;
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
     * Add new user and new cart.
     * <p>
     * This function is used to add a new user and new card for this user.
     * @param newUser
     * @return user information if user is added successfully.
     * @throws AppException(ResponseCode.NO_PARAM) if newUser is empty
     * @throws AppException(ResponseCode.MISSING_PARAM) if the passed-in parameter values such as
     * last name, first name, phone number, email, password or userRole are missing
     * @throws AppException(ResponseCode.DATA_ALREADY_EXISTS) if user be found by userName
     * @throws AppException(ResponseCode.INSERT_FAILURE) if is added failure
     * based on the passed-in ID parameter
     * @author HoangVu
     * @since 1.0
     */
    @PostMapping("")
    public ResponseEntity<?> addUser(@RequestBody Map<String, Object> newUser) {
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
        User foundUser = this.userService.findByUserName(newUser.get("userName").toString())
                .orElseThrow(() -> new AppException(ResponseCode.DATA_ALREADY_EXISTS));
        User insertUser = userService.addUser(newUser);
        if (!ObjectUtils.isEmpty(insertUser)) {
            Cart inserCart = cartService.addCart(foundUser);
            if (!ObjectUtils.isEmpty(inserCart)) {
                Map<String, Object> responseData = new HashMap<>();
                responseData.put("user", new UserDTOResponse(insertUser));
                responseData.put("cart", new CartDTOResponse(inserCart));
                return super.success(responseData);
            } else {
                throw new AppException(ResponseCode.INSERT_FAILURE);
            }
        } else {
            throw new AppException(ResponseCode.INSERT_FAILURE);
        }
    }
}
