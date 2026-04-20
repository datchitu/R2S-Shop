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
import org.modelmapper.ModelMapper;
import org.modelmapper.convention.MatchingStrategies;
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
    @Autowired
    private ModelMapper modelMapper;

    /**
     * Add new user.
     * <p>
     * This method is used to add a new user.
     * @param dtoRequest
     * @return information of user if the add process is successful
     * @throws AppException(ResponseCode.DATA_ALREADY_EXISTS) if user be found by userName
     * @throws AppException(ResponseCode.ROLE_NOT_FOUND) if role does not found by userRole
     * @author HoangVu
     * @since 1.4
     */
    @Transactional
    @Override
    public User add(UserRegistrationDTORequest dtoRequest) {
        if (userRepository.existsByUserName(dtoRequest.getUserName())) {
            throw new AppException(ResponseCode.DATA_ALREADY_EXISTS);
        }
        if (!roleRepository.existsByRoleName(dtoRequest.getUserRole())) {
            throw new AppException(ResponseCode.ROLE_NOT_FOUND);
        }
        User user = modelMapper.map(dtoRequest, User.class);
        user.setPassword(passwordEncoder.encode(dtoRequest.getPassword()));
        user.setRoles(this.roleRepository.findByRoleName(dtoRequest.getUserRole()));
        return userRepository.save(user);
    }
    /**
     * Find by userName.
     * <p>
     * This method is used find user by userName.
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
     * Find by id.
     * <p>
     * This method is used find user by id.
     * @param id
     * @return information of user if user is found
     * @throws AppException(ResponseCode.USER_NOT_FOUND) if the User cannot be found by id
     * @author HoangVu
     * @since 1.0
     */
    @Override
    public User findById(Long id) {
        return userRepository.findById(id)
                .orElseThrow(() -> new AppException(ResponseCode.USER_NOT_FOUND));
    }

    /**
     * Register user with cart by userName.
     * <p>
     * This method is used to insert the user and cart into the database., with the newUser as the input parameter.
     * @param dtoRequest
     * @return User and cart information if insertion is successful.
     * @throws AppException(ResponseCode.INSERT_FAILURE) if insertUser and insertCart is empty
     * @author HoangVu
     * @since 1.4
     */
    @Transactional
    @Override
    public Map<String, Object> registerWithCart(UserRegistrationDTORequest dtoRequest) {
        User insertUser = add(dtoRequest);
        if (ObjectUtils.isEmpty(insertUser)) {
            throw new AppException(ResponseCode.INSERT_FAILURE);
        }
        User foundUser = findByUserName(dtoRequest.getUserName());
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
     * This method returns all user by deleted (With the passed-in status -1, return all works;
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
     * This method updates user by userName, with the userName as the input parameter.
     * @param userName
     * @param dtoRequest
     * @return User by userName if the update process is successful
     * @throws AppException(ResponseCode.USER_NOT_FOUND) if user does not exist in the database
     * @author HoangVu
     * @since 1.4
     */
    @Transactional
    @Override
    public User update(String userName, UserUpdateDTORequest dtoRequest) {
        User foundUser = findByUserName(userName);
        modelMapper.getConfiguration().setMatchingStrategy(MatchingStrategies.STRICT);
        modelMapper.map(dtoRequest, foundUser);
        return userRepository.save(foundUser);
    }
    /**
     * Charge user password by userName.
     * <p>
     * This method charges user password by userName, with the userName, newPassword as the input parameter.
     * @param userName
     * @param newPassword
     * @throws AppException(ResponseCode.USER_NOT_FOUND) if user does not exist in the database
     * @throws AppException(ResponseCode.NOT_MATCH_PASSWORD) if old password does not match in the database
     * @throws AppException(ResponseCode.DUPLICATE_PASSWORD) if new password is the same as the current password
     * in the database
     * @author HoangVu
     * @since 1.3
     */
    @Override
    public void chargePassword(String userName, String oldPassword, String newPassword) {
        User foundUser = findByUserName(userName);
        if (!passwordEncoder.matches(oldPassword, foundUser.getPassword())) {
            throw new AppException(ResponseCode.NOT_MATCH_PASSWORD);
        }
        if (passwordEncoder.matches(newPassword, foundUser.getPassword())) {
            throw new AppException(ResponseCode.DUPLICATE_PASSWORD);
        }
        foundUser.setPassword(passwordEncoder.encode(newPassword));
        userRepository.save(foundUser);
    }
    /**
     * Delete user by id.
     * <p>
     * This method deletes user by id, with the id as the input parameter.
     * @param id
     * @throws AppException(ResponseCode.USER_NOT_FOUND)
     * if user does not exist in the database
     * @throws AppException(ResponseCode.USER_ALREADY_DELETED)
     * if user already been deleted in the database
     * @author HoangVu
     * @since 1.0
     */
    @Override
    public void deleteById(Long id) {
        User foundUser = findById(id);
        if (Boolean.TRUE.equals(foundUser.getDeleted())) {
            throw new AppException(ResponseCode.USER_ALREADY_DELETED);
        }
        foundUser.setDeleted(true);
        userRepository.save(foundUser);
    }
    /**
     * Reactivate user by id.
     * <p>
     * This method reactivates user by id, with the id as the input parameter.
     * @param id
     * @throws AppException(ResponseCode.USER_NOT_FOUND)
     * if user does not exist in the database
     * @throws AppException(ResponseCode.USER_ALREADY_REACTIVATED)
     * if user already been reactivated in the database
     * @author HoangVu
     * @since 1.0
     */
    @Override
    public void reactivateById(Long id) {
        User foundUser = findById(id);
        if (Boolean.FALSE.equals(foundUser.getDeleted())) {
            throw new AppException(ResponseCode.USER_ALREADY_REACTIVATED);
        }
        foundUser.setDeleted(false);
        userRepository.save(foundUser);
    }
    /**
     * Block user by id.
     * <p>
     * This method blocks user by id, with the id as the input parameter.
     * @param id
     * @throws AppException(ResponseCode.USER_NOT_FOUND)
     * if user does not exist in the database
     * @throws AppException(ResponseCode.USER_ALREADY_BLOCKED)
     * if user already been blocked in the database
     * @author HoangVu
     * @since 1.0
     */
    @Override
    public void blockById(Long id) {
        User foundUser = findById(id);
        if (Boolean.TRUE.equals(foundUser.getDeleted())) {
            throw new AppException(ResponseCode.USER_ALREADY_BLOCKED);
        }
        foundUser.setStatus(true);
        userRepository.save(foundUser);
    }
    /**
     * Unblock user by id.
     * <p>
     * This method unblocks user by id, with the id as the input parameter.
     * @param id
     * @throws AppException(ResponseCode.USER_NOT_FOUND)
     * if user does not exist in the database
     * @throws AppException(ResponseCode.USER_ALREADY_UNBLOCKED)
     * if user already been unblocked in the database
     * @author HoangVu
     * @since 1.0
     */
    @Override
    public void unblockById(Long id) {
        User foundUser = findById(id);
        if (Boolean.FALSE.equals(foundUser.getStatus())) {
            throw new AppException(ResponseCode.USER_ALREADY_UNBLOCKED);
        }
        foundUser.setStatus(false);
        userRepository.save(foundUser);
    }
}
