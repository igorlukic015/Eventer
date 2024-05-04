package com.eventer.user.service.impl;

import com.eventer.user.contracts.auth.AuthenticationResponse;
import com.eventer.user.contracts.auth.LoginRequest;
import com.eventer.user.contracts.auth.RegisterRequest;
import com.eventer.user.data.repository.UserRepository;
import com.eventer.user.mapper.UserMapper;
import com.eventer.user.security.service.JwtService;
import com.eventer.user.service.UserService;
import com.eventer.user.service.domain.User;
import com.eventer.user.utils.ResultErrorMessages;
import com.github.igorlukic015.resulter.Result;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.argon2.Argon2PasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.interceptor.TransactionAspectSupport;

@Service
public class UserServiceImpl implements UserService {
    private final AuthenticationManager authenticationManager;
    private final JwtService jwtService;
    private final UserRepository userRepository;
    private static final Logger logger = LoggerFactory.getLogger(UserServiceImpl.class);

    public UserServiceImpl(AuthenticationManager authenticationManager, JwtService jwtService, UserRepository userRepository) {
        this.authenticationManager = authenticationManager;
        this.jwtService = jwtService;
        this.userRepository = userRepository;
    }

    @Transactional(readOnly = true)
    @Override
    public Result<AuthenticationResponse> authenticate(LoginRequest loginRequest) {
        logger.info("Attempting to authenticate {}", loginRequest.username());

        if (!this.userRepository.existsByUsername(loginRequest.username())) {
            logger.error(ResultErrorMessages.userNotFound);
            return Result.unauthorized(ResultErrorMessages.userNotFound);
        }

        var authenticationToken =
                new UsernamePasswordAuthenticationToken(
                        loginRequest.username(), loginRequest.password());

        var authentication = authenticationManager.authenticate(authenticationToken);

        SecurityContextHolder.getContext().setAuthentication(authentication);
        String accessToken = jwtService.generateToken(loginRequest.username());

        logger.info("Authentication success for {}", loginRequest.username());

        return Result.success(
                new AuthenticationResponse(accessToken));
    }

    @Transactional
    @Override
    public Result<User> register(RegisterRequest request) {
        logger.info("Attempting to create {}", User.class.getSimpleName());

        if (this.userRepository.existsByUsername(request.username())) {
            logger.error(ResultErrorMessages.userUsernameConflicted);
            TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
            return Result.conflict(ResultErrorMessages.userUsernameConflicted);
        }

        PasswordEncoder passwordEncoder = Argon2PasswordEncoder.defaultsForSpringSecurity_v5_8();
        String encodedPassword = passwordEncoder.encode(request.password());

        Result<User> userOrError = User.create(request.username(), encodedPassword, null);

        if (userOrError.isFailure()) {
            logger.error(userOrError.getMessage());
            TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
            return Result.fromError(userOrError);
        }

        com.eventer.user.data.model.User user = UserMapper.toModel(userOrError.getValue());

        user = this.userRepository.save(user);

        logger.info("User successfully saved");

        return UserMapper.toDomain(user);
    }
}
