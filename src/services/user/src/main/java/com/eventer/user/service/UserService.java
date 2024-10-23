package com.eventer.user.service;

import com.eventer.user.contracts.auth.*;
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
    Result requestPasswordReset(String email);
    Result resetPassword(PasswordResetRequest request);
}
