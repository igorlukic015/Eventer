package com.eventer.user.service;

import com.eventer.user.contracts.auth.AuthenticationResponse;
import com.eventer.user.contracts.auth.LoginRequest;
import com.eventer.user.contracts.auth.RegisterRequest;
import com.eventer.user.service.domain.User;
import com.github.igorlukic015.resulter.Result;

public interface UserService {
    Result<AuthenticationResponse> authenticate(LoginRequest loginRequest);
    Result<User> register(RegisterRequest request);
}
