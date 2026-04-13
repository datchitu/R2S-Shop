package com.r2s.R2Sshop.service;

import com.r2s.R2Sshop.DTO.UserRegistrationDTORequest;
import com.r2s.R2Sshop.DTO.UserUpdateDTORequest;
import com.r2s.R2Sshop.model.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.Map;

public interface UserService {
    User addUser(UserRegistrationDTORequest newUser);
    User findByUserName(String userName);
    User findById(Long id);
    Map<String, Object> registerUserWithCart(UserRegistrationDTORequest newUser);
    Page<User> getAllByDeleted(Integer status, Pageable pageable);
    User updateUser(String userName, UserUpdateDTORequest user);
    void chargePassword(String userName, String oldPassword, String newPassword);
    void deleteById(Long id);
    void reactivateById(Long id);
    void blockById(Long id);
    void unblockById(Long id);
}
