package com.r2s.R2Sshop.rest;


import com.r2s.R2Sshop.DTO.AuthenDTORequest;
import com.r2s.R2Sshop.DTO.AuthenDTOResponse;
import com.r2s.R2Sshop.constants.ResponseCode;
import com.r2s.R2Sshop.repository.UserRepository;
import com.r2s.R2Sshop.utils.JwtUtils;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
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
     * Signin.
     * <p>
     * This method is used to authenticate signin information from username and password.
     * Then returns a token and a message.
     * @param authen
     * @return token and "Successful signin!".
     * @throws AppException(ResponseCode.FAILURE_SIGNIN) if response is empty(authentication failed)
     * @throws ResponseCode.INVALID_VALUE if the passed-in parameter values such as userName and password are missing
     * @author HoangVu
     * @since 1.2
     */
    @PostMapping
    public ResponseEntity<?> signin(@Valid @RequestBody AuthenDTORequest authen) {
        this.authenticationManager
                .authenticate(new UsernamePasswordAuthenticationToken(authen.getUserName(), authen.getPassword()));
        String token = JwtUtils.generateToken(authen.getUserName());
        AuthenDTOResponse response = new AuthenDTOResponse(token, "Successful signin!");
        return success(response);
    }
}


