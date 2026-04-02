package com.r2s.R2Sshop.service;

import com.r2s.R2Sshop.model.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.Map;
import java.util.Optional;

public interface UserService {
    User addUser(Map<String, Object> newUser);
    Optional<User> findByUserName(String userName);
    Boolean existsByUserName(String userName);
    Map<String, Object> registerUserWithCart(Map<String, Object> newUser);
    Page<User> getAll(Pageable pageable);
    Page<User> getAllByDeleted(Boolean deleted, Pageable pageable);
}
