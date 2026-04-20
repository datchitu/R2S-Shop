package com.r2s.R2Sshop.rest;

import com.r2s.R2Sshop.DTO.*;
import com.r2s.R2Sshop.constants.ResponseCode;
import com.r2s.R2Sshop.model.Cart;
import com.r2s.R2Sshop.model.User;
import com.r2s.R2Sshop.service.UserService;
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
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@RestController
@RequestMapping(path = "/users")
public class UserController extends BaseRestController{
    @Autowired
    private UserService userService;

    /**
     * Add new user with new cart.
     * <p>
     * This method is used to add a new user with new card for this user.
     * @param dtoRequest
     * @return user with cart information if user with cart is added successfully.
     * @throws AppException(ResponseCode.NO_PARAM) if newUser is empty
     * @throws AppException(ResponseCode.INVALID_VALUE) if the passed-in parameter values such as
     * last name, first name, phone number, email, password or userRole are missing
     * @author HoangVu
     * @since 1.5
     */
    @PostMapping
    public ResponseEntity<?> addWithCart(@Valid @RequestBody UserRegistrationDTORequest dtoRequest) {
        if  (ObjectUtils.isEmpty(dtoRequest)) {
            throw new AppException(ResponseCode.NO_PARAM);
        }
        Map<String, Object> data = userService.registerWithCart(dtoRequest);
        Map<String, Object> responseData = new HashMap<>();
        responseData.put("user", new UserDTOResponse((User) data.get("user")));
        responseData.put("cart", new CartDTOResponse((Cart) data.get("cart")));
        return super.success(responseData);
    }
    /**
     * Return user list.
     * <p>
     * This method returns user list by status(With the passed-in status -1, return all works;
     * with 0, return all by deleted == false works;
     * and otherwise, it's return all by deleted == true),
     * with the status as the input parameter.
     * @param status
     * @return the user list entity if the data is retrieved successfully.
     * @throws AppException(ResponseCode.INVALID_PARAM) if status is outside the value (-1, 0, 1)
     * @author HoangVu
     * @since 1.5
     */
    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping
    public ResponseEntity<?> getAllByDeleted(@RequestParam(defaultValue = "-1") Integer status,
                                             @RequestParam(defaultValue = "0") Integer offset,
                                             @RequestParam(defaultValue = "2") Integer limit) {
        if (!Arrays.asList(-1, 0, 1).contains(status)) {
            throw new AppException(ResponseCode.INVALID_PARAM);
        }
        Pageable pageable = PageRequest.of(offset, limit, Sort.by("id").ascending());
        Page<User> userPage = userService.getAllByDeleted(status, pageable);
        List<UserDTOResponse> responses = userPage.stream()
                .map(UserDTOResponse :: new)
                .collect(Collectors.toList());
        return super.success(responses);
    }
    /**
     * Return user info from token(userName).
     * <p>
     * This method returns user info from token(userName).
     * @return user info entity from token(userName)
     * @author HoangVu
     * @since 1.1
     */
    @GetMapping("/get-my-profile")
    public ResponseEntity<?> getMyProfile(@AuthenticationPrincipal UserDetails userDetails) {
        User foundUser = userService.findByUserName(userDetails.getUsername());
        return super.success(new UserDTOResponse(foundUser));
    }

    /**
     * Update ỉnfo user.
     * <p>
     * This method returns user info from token(userName) after successful update.
     * @param userDTORequest
     * @return user info entity from token(userName)
     * @throws AppException(ResponseCode.NO_PARAM) if userDTORequest is empty
     * @throws AppException(ResponseCode.INVALID_VALUE) if the passed-in parameter values such as
     * firstName, lastName, email, phone
     * @throws AppException(ResponseCode.FAILURE_USER_UPDATE) if update unsuccessful
     * @author HoangVu
     * @since 1.5
     */
    @PutMapping
    public ResponseEntity<?> updateProfile(@AuthenticationPrincipal UserDetails userDetails,
                                    @Valid @RequestBody UserUpdateDTORequest userDTORequest) {
        if (ObjectUtils.isEmpty(userDTORequest)) {
            throw new AppException(ResponseCode.NO_PARAM);
        }
        User updateUser = userService.update(userDetails.getUsername(), userDTORequest);
        return super.success(new UserDTOResponse(updateUser));
    }
    /**
     * Charge password user.
     * <p>
     * This method charges user password.
     * @return success
     * @throws AppException(ResponseCode.NO_PARAM) if userDTORequest is empty
     * @throws AppException(ResponseCode.MISSING_PARAM) if the passed-in parameter values such as
     * oldPassword, newPassword
     * @throws AppException(ResponseCode.FAILURE_PASSWORD_CHARGE) if charge failure
     * @author HoangVu
     * @since 1.4
     */
    @PutMapping("/change-password")
    public ResponseEntity<?> chargePassword(@AuthenticationPrincipal UserDetails userDetails,
                                            @Valid @RequestBody PasswordDTORequest password) {
        if (ObjectUtils.isEmpty(password)) {
            throw new AppException(ResponseCode.NO_PARAM);
        }
        userService.chargePassword(userDetails.getUsername(),
                password.getOldPassword(), password.getNewPassword());
        return super.success("Charged successfully");
    }
    /**
     * Delete user by id.
     * <p>
     * This method delete user by id, with the id as the input parameter.
     * @param id
     * @author HoangVu
     * @since 1.0
     */
    @PreAuthorize("hasAnyRole('ADMIN', 'USER')")
    @DeleteMapping("/delete-by-id")
    public ResponseEntity<?> deleteById(@RequestParam Long id) {
        userService.deleteById(id);
        return super.success("Deleted successfully");
    }
    /**
     * Reactivate user by id.
     * <p>
     * This method reactivate user by id, with the id as the input parameter.
     * @param id
     * @author HoangVu
     * @since 1.0
     */
    @PreAuthorize("hasRole('ADMIN')")
    @PutMapping("/reactivate-by-id")
    public ResponseEntity<?> reactivateById(@RequestParam Long id) {
        userService.reactivateById(id);
        return super.success("Reactivated successfully");
    }
    /**
     * Block user by id.
     * <p>
     * This method block user by id, with the id as the input parameter.
     * @param id
     * @author HoangVu
     * @since 1.1
     */
    @PreAuthorize("hasRole('ADMIN')")
    @DeleteMapping("/block-by-id")
    public ResponseEntity<?> blockById(@RequestParam Long id) {
        userService.blockById(id);
        return super.success("Blocked successfully");
    }
    /**
     * Unblock user by id.
     * <p>
     * This method unblock user by id, with the id as the input parameter.
     * @param id
     * @author HoangVu
     * @since 1.1
     */
    @PreAuthorize("hasRole('ADMIN')")
    @PutMapping("/unblock-by-id")
    public ResponseEntity<?> unblockById(@RequestParam Long id) {
        userService.unblockById(id);
        return super.success("Unblocked successfully");
    }
}
