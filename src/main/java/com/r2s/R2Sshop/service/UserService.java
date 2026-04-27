package com.r2s.R2Sshop.service;

import com.r2s.R2Sshop.DTO.UserRegistrationDTORequest;
import com.r2s.R2Sshop.DTO.UserUpdateDTORequest;
import com.r2s.R2Sshop.model.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.Map;

public interface UserService {
    User add(UserRegistrationDTORequest newUser);
    User findByUserName(String userName);
    User findById(Long id);
    Map<String, Object> registerWithCart(UserRegistrationDTORequest newUser);
    Page<User> getAllByDeleted(Integer status, Pageable pageable);
    User update(String userName, UserUpdateDTORequest user);
    void chargePassword(String userName, String oldPassword, String newPassword);
    void deleteById(Long id);
    void restoreById(Long id);
    void lockById(Long id);
    void lockPermanentlyById(Long id);
    void unlockById(Long id);
}
