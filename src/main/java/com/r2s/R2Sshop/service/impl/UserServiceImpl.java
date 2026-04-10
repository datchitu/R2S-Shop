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
import com.r2s.R2Sshop.service.UserService;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.util.ObjectUtils;

import java.util.HashMap;
import java.util.Map;

@Service
public class UserServiceImpl implements UserService {
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private PasswordEncoder passwordEncoder;
    @Autowired
    private RoleRepository roleRepository;
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
     * @since 1.2
     */
    @Override
    public User addUser(UserRegistrationDTORequest newUser) {
        if (userRepository.existsByUserName(newUser.getUserName())) {
            throw new AppException(ResponseCode.DATA_ALREADY_EXISTS);
        }
        if (!roleRepository.existsByRoleName(newUser.getUserRole())) {
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
        return userRepository.save(user);
    }
    /**
     * Find by userName.
     * <p>
     * This function is used find user by userName.
     * @param userName
     * @return information of user if user is found
     * @throws AppException(ResponseCode.USER_NOT_FOUND) if the User cannot be found by userName
     * @author HoangVu
     * @since 1.1
     */
    @Override
    public User findByUserName(String userName) {
        return userRepository.findByUserName(userName)
                .orElseThrow(() -> new AppException(ResponseCode.USER_NOT_FOUND));
    }

    /**
     * Register user with cart by userName.
     * <p>
     * This function is used to insert the user and cart into the database., with the newUser as the input parameter.
     * @param newUser
     * @return User and cart information if insertion is successful.
     * @throws AppException(ResponseCode.INSERT_FAILURE) if insertUser and insertCart is empty
     * @author HoangVu
     * @since 1.1
     */
    @Transactional
    @Override
    public Map<String, Object> registerUserWithCart(UserRegistrationDTORequest newUser) {
        User insertUser = addUser(newUser);
        if (ObjectUtils.isEmpty(insertUser)) {
            throw new AppException(ResponseCode.INSERT_FAILURE);
        }
        User foundUser = findByUserName(newUser.getUserName());
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
     * This function returns all user by deleted (With the passed-in status -1, return all works;
     * with 0, return all by deleted == false works;
     * and otherwise, it's return all by deleted == true),
     * with the status and pageable as the input parameter.
     * @param status
     * @param pageable
     * @return User list by deleted
     * @author HoangVu
     * @since 1.1
     */
    @Override
    public Page<User> getAllByDeleted(Integer status, Pageable pageable) {
        if (status == -1) {
            return userRepository.findAllByDeleted(null, pageable);
        } else if (status == 0) {
            return userRepository.findAllByDeleted(false, pageable);
        } else {
            return userRepository.findAllByDeleted(true, pageable);
        }
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
     * @since 1.2
     */
    @Override
    public User updateUser(String userName, UserUpdateDTORequest userDTORequest) {
        User foundUser = findByUserName(userName);
        foundUser.setFirstName(userDTORequest.getFirstName());
        foundUser.setLastName(userDTORequest.getLastName());
        foundUser.setEmail(userDTORequest.getEmail());
        foundUser.setPhone(userDTORequest.getPhone());
        return userRepository.save(foundUser);
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
     * @since 1.2
     */
    @Override
    public User chargePassword(String userName, String oldPassword, String newPassword) {
        User foundUser = findByUserName(userName);
        if (!passwordEncoder.matches(oldPassword, foundUser.getPassword())) {
            throw new AppException(ResponseCode.NOT_MATCH_PASSWORD);
        }
        if (passwordEncoder.matches(newPassword, foundUser.getPassword())) {
            throw new AppException(ResponseCode.DUPLICATE_PASSWORD);
        }
        foundUser.setPassword(passwordEncoder.encode(newPassword));
        return userRepository.save(foundUser);
    }
}
