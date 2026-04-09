package com.r2s.R2Sshop.service;

import com.r2s.R2Sshop.DTO.UserRegistrationDTORequest;
import com.r2s.R2Sshop.DTO.UserUpdateDTORequest;
import com.r2s.R2Sshop.model.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.Map;
import java.util.Optional;

public interface UserService {
    User addUser(UserRegistrationDTORequest newUser);
    Optional<User> findByUserName(String userName);
    Boolean existsByUserName(String userName);
    Map<String, Object> registerUserWithCart(UserRegistrationDTORequest newUser);
    Page<User> getAllByDeleted(Boolean deleted, Pageable pageable);
    User updateUser(String userName, UserUpdateDTORequest user);
    User chargePassword(String userName, String oldPassword, String newPassword);
}
