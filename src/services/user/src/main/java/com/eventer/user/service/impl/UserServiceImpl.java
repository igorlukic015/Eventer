package com.eventer.user.service.impl;

import com.eventer.user.contracts.auth.AuthenticationResponse;
import com.eventer.user.contracts.auth.LoginRequest;
import com.eventer.user.contracts.auth.RegisterRequest;
import com.eventer.user.contracts.auth.UpdateProfileRequest;
import com.eventer.user.data.repository.UserRepository;
import com.eventer.user.mapper.ImageMapper;
import com.eventer.user.mapper.UserMapper;
import com.eventer.user.security.contracts.CustomUserDetails;
import com.eventer.user.security.service.JwtService;
import com.eventer.user.service.UserService;
import com.eventer.user.service.domain.Image;
import com.eventer.user.service.domain.User;
import com.eventer.user.utils.ResultErrorMessages;
import com.github.igorlukic015.resulter.Result;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.argon2.Argon2PasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.interceptor.TransactionAspectSupport;

import java.util.Objects;
import java.util.Optional;
import java.util.Set;

@Service
public class UserServiceImpl implements UserService {
    private final AuthenticationManager authenticationManager;
    private final JwtService jwtService;
    private final UserRepository userRepository;
    private static final Logger logger = LoggerFactory.getLogger(UserServiceImpl.class);

    public UserServiceImpl(
            AuthenticationManager authenticationManager,
            JwtService jwtService,
            UserRepository userRepository) {
        this.authenticationManager = authenticationManager;
        this.jwtService = jwtService;
        this.userRepository = userRepository;
    }

    @Transactional(readOnly = true)
    @Override
    public Result<AuthenticationResponse> authenticate(LoginRequest loginRequest) {
        logger.info("Attempting to authenticate {}", loginRequest.username());

        var foundUser = this.userRepository.findByUsername(loginRequest.username());

        if (foundUser.isEmpty()) {
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

        var imageOrError = ImageMapper.toDomain(foundUser.get().getProfileImage());

        return Result.success(new AuthenticationResponse(accessToken, imageOrError.getValue() == null ? null : imageOrError.getValue().getUrl()));
    }

    @Transactional
    @Override
    public Result<User> register(RegisterRequest request) {
        logger.info("Attempting to create {}", User.class.getSimpleName());

        if (request.password().length() < 6) {
            logger.error("PASSWORD_TOO_SHORT");
            TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
            return Result.invalid("PASSWORD_TOO_SHORT");
        }

        if (this.userRepository.existsByUsername(request.username())) {
            logger.error(ResultErrorMessages.userUsernameConflicted);
            TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
            return Result.conflict(ResultErrorMessages.userUsernameConflicted);
        }

        PasswordEncoder passwordEncoder = Argon2PasswordEncoder.defaultsForSpringSecurity_v5_8();
        String encodedPassword = passwordEncoder.encode(request.password());

        Result<User> userOrError =
                User.create(request.name(), request.username(), encodedPassword, request.city());

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

    @Transactional(readOnly = true)
    @Override
    public Result<User> getProfileData(String username) {
        logger.info("Attempting to get profile data for user: {}", username);

        Optional<com.eventer.user.data.model.User> foundUser =
                this.userRepository.findByUsername(username);

        if (foundUser.isEmpty()) {
            logger.error("USER_NOT_FOUND");
            return Result.notFound("USER_NOT_FOUND");
        }

        logger.info("User successfully found");

        return UserMapper.toDomain(foundUser.get());
    }

    @Transactional
    @Override
    public Result<User> updateProfile(UpdateProfileRequest request) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        if (authentication == null || !authentication.isAuthenticated() || !(authentication.getPrincipal() instanceof CustomUserDetails)
                || !Objects.equals(
                        request.username(), ((CustomUserDetails) authentication.getPrincipal()).getUsername())) {
            return Result.invalid("UPDATING_DIFFERENT_PROFILE");
        }

        Optional<com.eventer.user.data.model.User> foundUser =
                this.userRepository.findByUsername(request.username());

        if (foundUser.isEmpty()) {
            return Result.invalid(ResultErrorMessages.userNotFound);
        }

        foundUser.get().setName(request.name());
        foundUser.get().setCity(request.city());

        var result = this.userRepository.save(foundUser.get());

        return UserMapper.toDomain(result);
    }

    @Transactional
    @Override
    public Result<User> updateProfileImage(Set<String> imageUrlSet) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        if (authentication == null
                || !authentication.isAuthenticated()
                || !(authentication.getPrincipal() instanceof UserDetails userDetails)) {
            return Result.invalid("UPDATING_DIFFERENT_PROFILE");
        }

        Optional<com.eventer.user.data.model.User> foundUser =
                this.userRepository.findByUsername(userDetails.getUsername());

        if (foundUser.isEmpty()) {
            return null;
        }

        Result<Image> newImage = Image.create(imageUrlSet.stream().toList().getFirst());

        if (newImage.isFailure()) {
            return Result.fromError(newImage);
        }

        foundUser.get().setProfileImage(ImageMapper.toModel(newImage.getValue()));

        var result = this.userRepository.save(foundUser.get());

        return UserMapper.toDomain(result);
    }
}
