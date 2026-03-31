package com.r2s.R2Sshop.service.impl;

import com.r2s.R2Sshop.model.Cart;
import com.r2s.R2Sshop.model.User;
import com.r2s.R2Sshop.repository.RoleRepository;
import com.r2s.R2Sshop.repository.UserRepository;
import com.r2s.R2Sshop.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.sql.Timestamp;
import java.time.ZonedDateTime;
import java.util.Map;
import java.util.Optional;

@Service
public class UserServiceImpl implements UserService {
    private Timestamp ts = Timestamp.from(ZonedDateTime.now().toInstant());

    @Autowired
    private UserRepository userRepository;
    @Autowired
    private PasswordEncoder passwordEncoder;
    @Autowired
    private RoleRepository roleRepository;

    /**
     * Add new user.
     * <p>
     * This function is used to add a new user.
     * @param newUser
     * @return userRepository.save(user)
     * @author HoangVu
     * @since 1.0
     */
    @Override
    public User addUser(Map<String, Object> newUser) {
        User user = new User();
        user.setFirstName(newUser.get("firstName").toString());
        user.setLastName(newUser.get("lastName").toString());
        user.setUserName(newUser.get("userName").toString());
        user.setEmail(newUser.get("email").toString());
        user.setPassword(this.passwordEncoder.encode(newUser.get("password").toString()));
        user.setPhone(newUser.get("phone").toString());
        user.setDeleted(false);
        user.setRoles(this.roleRepository.findByRoleName(newUser.get("userRole").toString()));
        return this.userRepository.save(user);
    }
    /**
     * Find by userName.
     * <p>
     * This function is used find user by userName.
     * @param userName
     * @return findByUserName(userName)
     * @author HoangVu
     * @since 1.0
     */
    @Override
    public Optional<User> findByUserName(String userName) {
        return userRepository.findByUserName(userName);
    }
}
