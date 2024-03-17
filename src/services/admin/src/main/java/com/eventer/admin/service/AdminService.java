package com.eventer.admin.service;

import com.eventer.admin.contracts.auth.AuthenticationResponse;
import com.eventer.admin.contracts.auth.LoginRequest;
import com.eventer.admin.contracts.auth.RegisterRequest;
import com.eventer.admin.service.domain.Admin;
import com.github.igorlukic015.resulter.Result;
import org.springframework.data.domain.Pageable;

public interface AdminService {
    Result<Admin> register(RegisterRequest request);

    Result<Admin> getAdminByUsername(String username);

    Result<Admin> getEventManagers(Pageable pageable);

    Result<AuthenticationResponse> authenticate(LoginRequest loginRequest);
}
