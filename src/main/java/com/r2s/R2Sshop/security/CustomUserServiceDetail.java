package com.r2s.R2Sshop.security;


import com.r2s.R2Sshop.model.User;
import com.r2s.R2Sshop.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Optional;
@Service
public class CustomUserServiceDetail implements UserDetailsService {
    @Autowired
    private UserRepository userRepository;

    @Override
    public UserDetails loadUserByUsername(String userName) throws UsernameNotFoundException {
        Optional<User> user = this.userRepository.findByUserName(userName);
        if (user.isPresent()) {
            return new CustomUserDetail(user.get());
        } else {
            throw new RuntimeException("User not found");
        }
    }
}
