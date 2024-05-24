package com.eventer.user.mapper;

import com.eventer.user.contracts.auth.AuthenticationResponse;
import com.eventer.user.contracts.auth.LoginRequest;
import com.eventer.user.contracts.auth.RegisterRequest;
import com.eventer.user.contracts.auth.UpdateProfileRequest;
import com.eventer.user.service.domain.Image;
import com.eventer.user.service.domain.User;
import com.eventer.user.web.dto.auth.AuthenticationResponseDTO;
import com.eventer.user.web.dto.auth.LoginDTO;
import com.eventer.user.web.dto.auth.RegisterDTO;
import com.eventer.user.web.dto.user.UpdateProfileDTO;
import com.eventer.user.web.dto.user.UserDTO;
import com.github.igorlukic015.resulter.Result;

public class UserMapper {
    public static UpdateProfileRequest toRequest(UpdateProfileDTO dto) {
        return new UpdateProfileRequest(dto.id(), dto.username(), dto.name(), dto.city());
    }

    public static LoginRequest toRequest(LoginDTO dto) {
        return new LoginRequest(dto.username(), dto.password());
    }

    public static AuthenticationResponseDTO toDTO(AuthenticationResponse response) {
        return new AuthenticationResponseDTO(
                response.accessToken(), response.userProfileImageUrl());
    }

    public static RegisterRequest toRequest(RegisterDTO dto) {
        return new RegisterRequest(dto.name(), dto.username(), dto.password(), dto.city());
    }

    public static UserDTO toDTO(User user) {
        return new UserDTO(
                user.getId(), user.getUsername(), user.getName(), user.getCity(), ImageMapper.toDTO(user.getProfileImage()));
    }

    public static Result<User> toDomain(com.eventer.user.data.model.User user) {
        Result<Image> imageOrError = ImageMapper.toDomain(user.getProfileImage());

        return User.create(
                user.getId(),
                user.getName(),
                user.getUsername(),
                user.getPassword(),
                user.getCity(),
                imageOrError);
    }

    public static com.eventer.user.data.model.User toModel(User user) {
        var model = new com.eventer.user.data.model.User();

        model.setId(user.getId());
        model.setName(user.getName());
        model.setUsername(user.getUsername());
        model.setPassword(user.getPassword());
        model.setCity(user.getCity());
        model.setProfileImage(ImageMapper.toModel(user.getProfileImage()));

        return model;
    }
}
