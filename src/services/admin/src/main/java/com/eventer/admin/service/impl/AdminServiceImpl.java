package com.eventer.admin.service.impl;

import com.eventer.admin.contracts.auth.AuthenticationResponse;
import com.eventer.admin.contracts.auth.LoginRequest;
import com.eventer.admin.contracts.auth.RegisterRequest;
import com.eventer.admin.data.repository.AdminRepository;
import com.eventer.admin.mapper.AdminMapper;
import com.eventer.admin.security.service.JwtService;
import com.eventer.admin.service.AdminService;
import com.eventer.admin.service.domain.Admin;
import com.eventer.admin.service.domain.Role;
import com.github.cigor99.resulter.Result;
import com.eventer.admin.utils.ResultErrorMessages;
import org.springframework.data.domain.Pageable;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.argon2.Argon2PasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class AdminServiceImpl implements AdminService {
    private final AuthenticationManager authenticationManager;
    private final JwtService jwtService;
    private final AdminRepository adminRepository;

    public AdminServiceImpl(
            AuthenticationManager authenticationManager,
            JwtService jwtService,
            AdminRepository adminRepository) {
        this.authenticationManager = authenticationManager;
        this.jwtService = jwtService;
        this.adminRepository = adminRepository;
    }

    @Override
    public Result<Admin> register(RegisterRequest request) {
        if (this.adminRepository.existsByUsername(request.username())) {
            return Result.conflict(ResultErrorMessages.adminUsernameConflicted);
        }

        PasswordEncoder passwordEncoder = Argon2PasswordEncoder.defaultsForSpringSecurity_v5_8();
        String encodedPassword = passwordEncoder.encode(request.password());

        Result<Admin> adminOrError =
                Admin.create(request.username(), encodedPassword, Role.EVENT_MANAGER);

        if (adminOrError.isFailure()) {
            return Result.fromError(adminOrError);
        }

        com.eventer.admin.data.model.Admin admin = AdminMapper.toModel(adminOrError.getValue());

        admin = this.adminRepository.save(admin);

        return AdminMapper.toDomain(admin);
    }

    @Override
    public Result<Admin> getAdminByUsername(String username) {
        Optional<com.eventer.admin.data.model.Admin> foundAdmin = this.adminRepository.findByUsername(username);

        if (foundAdmin.isEmpty()) {
            return Result.notFound(ResultErrorMessages.adminNotFound);
        }

        return AdminMapper.toDomain(foundAdmin.get());
    }

    @Override
    public Result<Admin> getEventManagers(Pageable pageable) {
        return null;
    }

    @Override
    public Result<AuthenticationResponse> authenticate(LoginRequest loginRequest) {
        Result<Admin> adminOrError = this.getAdminByUsername(loginRequest.username());

        if (adminOrError.isFailure()) {
            return Result.unauthorized(adminOrError.getMessage());
        }

        var authenticationToken =
                new UsernamePasswordAuthenticationToken(
                        loginRequest.username(), loginRequest.password());

        var authentication = authenticationManager.authenticate(authenticationToken);

        SecurityContextHolder.getContext().setAuthentication(authentication);
        String accessToken = jwtService.generateToken(loginRequest.username());

        return Result.success(new AuthenticationResponse(accessToken));
    }
}
