package com.r2s.R2Sshop.rest;

import com.r2s.R2Sshop.DTO.CartDTOResponse;
import com.r2s.R2Sshop.DTO.PasswordDTORequest;
import com.r2s.R2Sshop.DTO.UserDTORequest;
import com.r2s.R2Sshop.DTO.UserDTOResponse;
import com.r2s.R2Sshop.constants.ResponseCode;
import com.r2s.R2Sshop.model.Cart;
import com.r2s.R2Sshop.model.User;
import com.r2s.R2Sshop.repository.CartRepository;
import com.r2s.R2Sshop.repository.UserRepository;
import com.r2s.R2Sshop.service.CartService;
import com.r2s.R2Sshop.service.UserService;
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
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

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
    /**
     * Return user list.
     * <p>
     * This function returns user list by status(With the passed-in status -1, return all works;
     * with 0, return all by deleted == false works;
     * and otherwise, it's return all by deleted == true),
     * with the status as the input parameter.
     * @param status
     * @return the user list entity if the data is retrieved successfully.
     * @throws AppException(ResponseCode.NO_PARAM) if status is outside the value (-1, 0, 1)
     * @author HoangVu
     * @since 1.2
     */
    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping("")
    public ResponseEntity<?> getAllByDeleted(@RequestParam(defaultValue = "-1") Integer status,
                                             @RequestParam(defaultValue = "0") Integer offset,
                                             @RequestParam(defaultValue = "2") Integer limit) {
        if (!Arrays.asList(-1, 0, 1).contains(status)) {
            throw new AppException(ResponseCode.INVALID_PARAM);
        }
        Pageable pageable = PageRequest.of(offset, limit, Sort.by("id").ascending());
        Page<User> userPage;
        if (status == -1) {
            userPage = userService.getAll(pageable);
        } else if (status == 0) {
            userPage = userService.getAllByDeleted(false, pageable);
        } else {
            userPage = userService.getAllByDeleted(true, pageable);
        }
        List<UserDTOResponse> responses = userPage.stream()
                .map(UserDTOResponse :: new)
                .collect(Collectors.toList());
        return super.success(responses);
    }
    /**
     * Return user info from token(userName).
     * <p>
     * This function returns user info from token(userName).
     * @return user info entity from token(userName)
     * @throws AppException(ResponseCode.USER_NOT_FOUND) if the User cannot be found by userName
     * @author HoangVu
     * @since 1.0
     */
    @GetMapping("/get-my-profile")
    public ResponseEntity<?> getMyProfile(@AuthenticationPrincipal UserDetails userDetails) {
        User foundUser = userService.findByUserName(userDetails.getUsername())
                .orElseThrow(() -> new AppException(ResponseCode.USER_NOT_FOUND));
        return super.success(new UserDTOResponse(foundUser));
    }

    /**
     * Update ỉnfo user.
     * <p>
     * This function returns user info from token(userName) after successful update.
     * @return user info entity from token(userName)
     * @throws AppException(ResponseCode.MISSING_PARAM) if the passed-in parameter values such as
     * firstName, lastName, email, phone
     * @throws AppException(ResponseCode.FAILURE_USER_UPDATE) if update unsuccessful
     * @author HoangVu
     * @since 1.0
     */
    @PutMapping("")
    public ResponseEntity<?> updateProfile(@AuthenticationPrincipal UserDetails userDetails,
                                    @RequestBody(required = false) Map<String, Object> user) {
        UserDTORequest userDTORequest = new UserDTORequest(user);
        if (ObjectUtils.isEmpty(userDTORequest.getFirstName())
                || ObjectUtils.isEmpty(userDTORequest.getLastName())
                || ObjectUtils.isEmpty(userDTORequest.getEmail())
                || ObjectUtils.isEmpty(userDTORequest.getPhone())) {
            throw new AppException(ResponseCode.MISSING_PARAM);
        }
        User updateUser = userService.updateUser(userDetails.getUsername(), user);
        if (ObjectUtils.isEmpty(updateUser)) {
            throw new AppException(ResponseCode.FAILURE_USER_UPDATE);
        }
        return super.success(new UserDTOResponse(updateUser));
    }
    /**
     * Charge password user.
     * <p>
     * This function charges user password.
     * @return success
     * @throws AppException(ResponseCode.MISSING_PARAM) if the passed-in parameter values such as
     * oldPassword, newPassword
     * @throws AppException(ResponseCode.FAILURE_PASSWORD_CHARGE) if charge failure
     * @author HoangVu
     * @since 1.0
     */
    @PutMapping("/change-password")
    public ResponseEntity<?> chargePassword(@AuthenticationPrincipal UserDetails userDetails,
                                            @RequestBody(required = false) PasswordDTORequest password) {
        if (ObjectUtils.isEmpty(password.getOldPassword())
                || ObjectUtils.isEmpty(password.getNewPassword())) {
            throw new AppException(ResponseCode.MISSING_PARAM);
        }
        User chargePasswordUser = userService.chargePassword(userDetails.getUsername(),
                password.getOldPassword(), password.getNewPassword());
        if (ObjectUtils.isEmpty(chargePasswordUser)) {
            throw new AppException(ResponseCode.FAILURE_PASSWORD_CHARGE);
        }
        return super.success(null);
    }
}
