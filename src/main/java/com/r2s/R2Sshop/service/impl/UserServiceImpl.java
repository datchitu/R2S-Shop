package com.r2s.R2Sshop.service.impl;

import com.r2s.R2Sshop.DTO.UserRegistrationDTORequest;
import com.r2s.R2Sshop.DTO.UserUpdateDTORequest;
import com.r2s.R2Sshop.constants.ResponseCode;
import com.r2s.R2Sshop.model.Cart;
import com.r2s.R2Sshop.model.User;
import com.r2s.R2Sshop.repository.RoleRepository;
import com.r2s.R2Sshop.repository.UserRepository;
import com.r2s.R2Sshop.rest.AppException;
import com.r2s.R2Sshop.service.CartService;
import com.r2s.R2Sshop.service.RoleService;
import com.r2s.R2Sshop.service.UserService;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.util.ObjectUtils;

import java.sql.Timestamp;
import java.time.ZonedDateTime;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

@Service
public class UserServiceImpl implements UserService {
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private PasswordEncoder passwordEncoder;
    @Autowired
    private RoleRepository roleRepository;
    @Autowired
    private RoleService roleService;
    @Autowired
    private CartService cartService;

    /**
     * Add new user.
     * <p>
     * This function is used to add a new user.
     * @param newUser
     * @return information of user if the add process is successful
     * @throws AppException(ResponseCode.DATA_ALREADY_EXISTS) if user be found by userName
     * @throws AppException(ResponseCode.ROLE_NOT_FOUND) if role does not found by userRole
     * @author HoangVu
     * @since 1.1
     */
    @Override
    public User addUser(UserRegistrationDTORequest newUser) {
        if (existsByUserName(newUser.getUserName())) {
            throw new AppException(ResponseCode.DATA_ALREADY_EXISTS);
        }
        if (!roleService.existsByRoleName(newUser.getUserRole())) {
            throw new AppException(ResponseCode.ROLE_NOT_FOUND);
        }
        User user = new User();
        user.setFirstName(newUser.getFirstName());
        user.setLastName(newUser.getLastName());
        user.setUserName(newUser.getUserName());
        user.setEmail(newUser.getEmail());
        user.setPassword(passwordEncoder.encode(newUser.getPassword()));
        user.setPhone(newUser.getPhone());
        user.setStatus(false);
        user.setDeleted(false);
        user.setRoles(this.roleRepository.findByRoleName(newUser.getUserRole()));
        return this.userRepository.save(user);
    }
    /**
     * Find by userName.
     * <p>
     * This function is used find user by userName.
     * @param userName
     * @return information of user if user is found
     * @author HoangVu
     * @since 1.0
     */
    @Override
    public Optional<User> findByUserName(String userName) {
        return userRepository.findByUserName(userName);
    }

    /**
     * Check exists user by userName.
     * <p>
     * This function check exists user by userName, with the userName as the input parameter.
     * @param userName
     * @return True if it exists and an error if it does not.
     * @author HoangVu
     * @since 1.0
     */
    @Override
    public Boolean existsByUserName(String userName) {
        return userRepository.existsByUserName(userName);
    }
    /**
     * Register user with cart by userName.
     * <p>
     * This function is used to insert the user and cart into the database., with the newUser as the input parameter.
     * @param newUser
     * @return User and cart information if insertion is successful.
     * @throws AppException(ResponseCode.INSERT_FAILURE) if insertUser and insertCart is empty
     * @throws AppException(ResponseCode.NOT_FOUND) if newUser does not exist in the database
     * @author HoangVu
     * @since 1.0
     */
    @Transactional
    @Override
    public Map<String, Object> registerUserWithCart(UserRegistrationDTORequest newUser) {
        User insertUser = addUser(newUser);
        if (ObjectUtils.isEmpty(insertUser)) {
            throw new AppException(ResponseCode.INSERT_FAILURE);
        }
        User foundUser = this.findByUserName(newUser.getUserName())
                .orElseThrow(() -> new AppException(ResponseCode.NOT_FOUND));
        Cart inserCart = cartService.addCart(foundUser);
        if (ObjectUtils.isEmpty(inserCart)) {
                throw new AppException(ResponseCode.INSERT_FAILURE);
        }
        Map<String, Object> result = new HashMap<>();
        result.put("user", insertUser);
        result.put("cart", inserCart);
        return result;
    }
    /**
     * Return user list by deleted.
     * <p>
     * This function returns all user by deleted, with the deleted as the input parameter.
     * @param deleted
     * @return User list by deleted
     * @author HoangVu
     * @since 1.0
     */
    @Override
    public Page<User> getAllByDeleted(Boolean deleted, Pageable pageable) {
        return userRepository.findAllByDeleted(deleted, pageable);
    }
    /**
     * Update user by userName.
     * <p>
     * This function updates user by userName, with the userName as the input parameter.
     * @param userName
     * @param userDTORequest
     * @return User by userName if the update process is successful
     * @throws AppException(ResponseCode.USER_NOT_FOUND) if user does not exist in the database
     * @author HoangVu
     * @since 1.1
     */
    @Override
    public User updateUser(String userName, UserUpdateDTORequest userDTORequest) {
        User foundUser = findByUserName(userName)
                .orElseThrow(() -> new AppException(ResponseCode.USER_NOT_FOUND));
        foundUser.setFirstName(userDTORequest.getFirstName());
        foundUser.setLastName(userDTORequest.getLastName());
        foundUser.setEmail(userDTORequest.getEmail());
        foundUser.setPhone(userDTORequest.getPhone());
        return this.userRepository.save(foundUser);
    }
    /**
     * Charge user password by userName.
     * <p>
     * This function charges user password by userName, with the userName, newPassword as the input parameter.
     * @param userName
     * @param newPassword
     * @return User by userName if the charge process is successful
     * @throws AppException(ResponseCode.USER_NOT_FOUND) if user does not exist in the database
     * @throws AppException(ResponseCode.NOT_MATCH_PASSWORD) if old password does not match in the database
     * @throws AppException(ResponseCode.DUPLICATE_PASSWORD) if new password is the same as the current password
     * in the database
     * @author HoangVu
     * @since 1.1
     */
    @Override
    public User chargePassword(String userName, String oldPassword, String newPassword) {
        User foundUser = findByUserName(userName)
                .orElseThrow(() -> new AppException(ResponseCode.USER_NOT_FOUND));
        if (!this.passwordEncoder.matches(oldPassword, foundUser.getPassword())) {
            throw new AppException(ResponseCode.NOT_MATCH_PASSWORD);
        }
        if (this.passwordEncoder.matches(newPassword, foundUser.getPassword())) {
            throw new AppException(ResponseCode.DUPLICATE_PASSWORD);
        }
        foundUser.setPassword(this.passwordEncoder.encode(newPassword));
        return this.userRepository.save(foundUser);
    }

}
