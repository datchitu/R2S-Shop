package com.r2s.R2Sshop.security;


import com.r2s.R2Sshop.constants.ResponseCode;
import com.r2s.R2Sshop.model.User;
import com.r2s.R2Sshop.repository.UserRepository;
import com.r2s.R2Sshop.rest.AppException;
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

    /**
     * Return user details by status and userName.
     * <p>
     * This method is used return user details by userName and status.
     * @param userName
     * @return information of user if user is found
     * @throws AppException(ResponseCode.USER_NOT_FOUND) if the User cannot be found by userName
     * @throws AppException(ResponseCode.IS_LOCKED_PERMANENT) if status = 2,
     * The account has been permanently looked
     * @throws AppException(ResponseCode.IS_LOCKED) if status = 1,
     * The account has been looked
     * @author HoangVu
     * @since 1.1
     */
    @Override
    public UserDetails loadUserByUsername(String userName) throws UsernameNotFoundException {
        User user = userRepository.findByUserName(userName)
                .orElseThrow(() -> new AppException(ResponseCode.USER_NOT_FOUND));
        if (user.getStatus() == 2) {
            throw new AppException(ResponseCode.IS_LOCKED_PERMANENT);
        } else if (user.getStatus() == 1) {
            throw new AppException(ResponseCode.IS_LOCKED);
        }

        return new CustomUserDetail(user);
    }
}
