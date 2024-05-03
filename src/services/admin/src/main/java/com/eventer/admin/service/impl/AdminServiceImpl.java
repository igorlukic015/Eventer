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
import com.github.igorlukic015.resulter.Result;
import com.eventer.admin.utils.ResultErrorMessages;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.argon2.Argon2PasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.interceptor.TransactionAspectSupport;

import java.util.Objects;
import java.util.Optional;

@Service
public class AdminServiceImpl implements AdminService {
    private final AuthenticationManager authenticationManager;
    private final JwtService jwtService;
    private final AdminRepository adminRepository;
    private static final Logger logger = LoggerFactory.getLogger(AdminServiceImpl.class);

    public AdminServiceImpl(
            AuthenticationManager authenticationManager,
            JwtService jwtService,
            AdminRepository adminRepository) {
        this.authenticationManager = authenticationManager;
        this.jwtService = jwtService;
        this.adminRepository = adminRepository;
    }

    @Transactional
    @Override
    public Result<Admin> register(RegisterRequest request) {
        logger.info("Attempting to create {}", Admin.class.getSimpleName());

        if (this.adminRepository.existsByUsername(request.username())) {
            logger.error(ResultErrorMessages.adminUsernameConflicted);
            TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
            return Result.conflict(ResultErrorMessages.adminUsernameConflicted);
        }

        PasswordEncoder passwordEncoder = Argon2PasswordEncoder.defaultsForSpringSecurity_v5_8();
        String encodedPassword = passwordEncoder.encode(request.password());

        Result<Admin> adminOrError =
                Admin.create(request.username(), encodedPassword, Role.EVENT_MANAGER);

        if (adminOrError.isFailure()) {
            logger.error(adminOrError.getMessage());
            TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
            return Result.fromError(adminOrError);
        }

        com.eventer.admin.data.model.Admin admin = AdminMapper.toModel(adminOrError.getValue());

        admin = this.adminRepository.save(admin);

        logger.info("Admin successfully saved");

        return AdminMapper.toDomain(admin);
    }

    @Transactional(readOnly = true)
    @Override
    public Result<Admin> getAdminByUsername(String username) {
        logger.info("Attempting to get {} by username {}", Admin.class.getSimpleName(), username);

        Optional<com.eventer.admin.data.model.Admin> foundAdmin =
                this.adminRepository.findByUsername(username);

        if (foundAdmin.isEmpty()) {
            logger.error(ResultErrorMessages.adminNotFound);
            return Result.notFound(ResultErrorMessages.adminNotFound);
        }

        logger.info(
                "{} with username {} found successfully", Admin.class.getSimpleName(), username);

        return AdminMapper.toDomain(foundAdmin.get());
    }

    @Transactional(readOnly = true)
    @Override
    public Result<Page<Admin>> getEventManagers(Pageable pageable, String searchTerm) {
        logger.info("Attempting to get event managers");

        Page<com.eventer.admin.data.model.Admin> foundAdmins =
                this.adminRepository.findAllByUsernameContainingIgnoreCaseAndRoleEqualsIgnoreCase(
                        searchTerm, Role.EVENT_MANAGER.getName(), pageable);

        Result<Page<Admin>> adminsOrError =
                AdminMapper.toDomainPage(foundAdmins);

        if (adminsOrError.isFailure()) {
            logger.error(adminsOrError.getMessage());
            return Result.fromError(adminsOrError);
        }

        return adminsOrError;
    }

    @Transactional
    @Override
    public Result delete(Long id) {
        logger.info("Attempting to delete admin");

        Optional<com.eventer.admin.data.model.Admin> foundAdmin = this.adminRepository.findById(id);

        if (foundAdmin.isEmpty()) {
            logger.error(ResultErrorMessages.adminNotFound);
            return Result.notFound(ResultErrorMessages.adminNotFound);
        }

        if (Objects.equals(foundAdmin.get().getRole(), Role.ADMINISTRATOR.getName())) {
            logger.error(ResultErrorMessages.deletingOfAdministratorsNotValid);
            return Result.invalid(ResultErrorMessages.deletingOfAdministratorsNotValid);
        }

        try {
            this.adminRepository.delete(foundAdmin.get());
        } catch (Exception e) {
            return Result.invalid(e.getMessage());
        }

        return Result.success();
    }

    @Transactional(readOnly = true)
    @Override
    public Result<AuthenticationResponse> authenticate(LoginRequest loginRequest) {
        logger.info("Attempting to authenticate {}", loginRequest.username());

        Result<Admin> adminOrError = this.getAdminByUsername(loginRequest.username());

        if (adminOrError.isFailure()) {
            logger.error(adminOrError.getMessage());
            return Result.unauthorized(adminOrError.getMessage());
        }

        var authenticationToken =
                new UsernamePasswordAuthenticationToken(
                        loginRequest.username(), loginRequest.password());

        var authentication = authenticationManager.authenticate(authenticationToken);

        SecurityContextHolder.getContext().setAuthentication(authentication);
        String accessToken = jwtService.generateToken(loginRequest.username());

        logger.info("Authentication success for {}", loginRequest.username());

        return Result.success(
                new AuthenticationResponse(
                        accessToken, adminOrError.getValue().getRole().getName()));
    }
}
