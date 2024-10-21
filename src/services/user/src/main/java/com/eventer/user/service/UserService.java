package com.eventer.user.service;

import com.eventer.user.contracts.auth.AuthenticationResponse;
import com.eventer.user.contracts.auth.LoginRequest;
import com.eventer.user.contracts.auth.RegisterRequest;
import com.eventer.user.contracts.auth.UpdateProfileRequest;
import com.eventer.user.service.domain.User;
import com.github.igorlukic015.resulter.Result;

import java.util.Set;

public interface UserService {
    Result<AuthenticationResponse> authenticate(LoginRequest loginRequest);
    Result<User> register(RegisterRequest request);
    Result<User> getProfileData(String username);
    Result<User> updateProfile(UpdateProfileRequest request);
    Result<User> updateProfileImage(Set<String> response);
    Result<Set<User>> getAll();
}
