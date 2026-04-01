package com.r2s.R2Sshop.service;

import com.r2s.R2Sshop.model.User;
import java.util.Map;
import java.util.Optional;

public interface UserService {
    User addUser(Map<String, Object> newUser);
    Optional<User> findByUserName(String userName);
    Boolean existsByUserName(String userName);
    Map<String, Object> registerUserWithCart(Map<String, Object> newUser);
}
