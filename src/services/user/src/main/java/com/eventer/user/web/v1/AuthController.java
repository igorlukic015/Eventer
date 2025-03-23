package com.eventer.user.web.v1;

import com.eventer.user.contracts.auth.AuthenticationResponse;
import com.eventer.user.mapper.UserMapper;
import com.eventer.user.service.UserService;
import com.eventer.user.service.domain.User;
import com.eventer.user.web.dto.auth.LoginDTO;
import com.eventer.user.web.dto.auth.RegisterDTO;
import com.github.igorlukic015.resulter.Result;
import com.github.igorlukic015.resulter.ResultUnwrapper;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/auth")
public class AuthController implements ResultUnwrapper {
    private final UserService userService;

    public AuthController(UserService userService) {
        this.userService = userService;
    }

    @PostMapping("/authenticate")
    public ResponseEntity<?> authorize(@RequestBody LoginDTO loginDTO) {
        Result<AuthenticationResponse> result =
                this.userService.authenticate(UserMapper.toRequest(loginDTO));
        return this.okOrError(result, UserMapper::toDTO);
    }

    @PostMapping("/register")
    public ResponseEntity<?> register(@RequestBody RegisterDTO registerDTO) {
        Result<User> result = this.userService.register(UserMapper.toRequest(registerDTO));
        return this.okOrError(result, UserMapper::toDTO);
    }
}
