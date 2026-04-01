package com.r2s.R2Sshop.rest;


import com.r2s.R2Sshop.DTO.AuthenDTORequest;
import com.r2s.R2Sshop.DTO.AuthenDTOResponse;
import com.r2s.R2Sshop.constants.ResponseCode;
import com.r2s.R2Sshop.repository.UserRepository;
import com.r2s.R2Sshop.utils.JwtUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.util.ObjectUtils;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(path = "/signin")
public class AuthenController extends BaseRestController {
    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private UserRepository userRepository;

    /**
     * Login.
     * <p>
     * This function is used to authenticate login information from username and password.
     * Then returns a token and a message.
     * @param authen
     * @return token and "Successful signin!".
     * @throws AppException(ResponseCode.INVALID_PARAM) if response is empty(authentication failed)
     * @author HoangVu
     * @since 1.0
     */
    @PostMapping
    public ResponseEntity<?> login(@RequestBody AuthenDTORequest authen) {
        this.authenticationManager
                    .authenticate(new UsernamePasswordAuthenticationToken(authen.getUserNane(), authen.getPassword()));
        String token = JwtUtils.generateToken(authen.getUserNane());
        AuthenDTOResponse response = new AuthenDTOResponse(token, "Successful signin!");
        if (ObjectUtils.isEmpty(response)) {
            throw new AppException(ResponseCode.INVALID_PARAM);
        }
        return success(response);
    }
}
