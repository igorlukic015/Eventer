package com.eventer.admin.mapper;

import com.eventer.admin.contracts.auth.AuthenticationResponse;
import com.eventer.admin.contracts.auth.LoginRequest;
import com.eventer.admin.contracts.auth.RegisterRequest;
import com.eventer.admin.service.domain.Admin;
import com.github.cigor99.resulter.Result;
import com.eventer.admin.web.dto.admin.AdminDTO;
import com.eventer.admin.web.dto.auth.AuthenticationResponseDTO;
import com.eventer.admin.web.dto.auth.LoginDTO;
import com.eventer.admin.web.dto.auth.RegisterDTO;

public class AdminMapper {

    public static LoginRequest toRequest(LoginDTO dto) {
        return new LoginRequest(dto.username(), dto.password());
    }

    public static AuthenticationResponseDTO toDTO(AuthenticationResponse response) {
        return new AuthenticationResponseDTO(response.accessToken());
    }

    public static RegisterRequest toRequest(RegisterDTO dto) {
        return new RegisterRequest(dto.username(), dto.password());
    }

    public static AdminDTO toDTO(Admin domain) {
        return new AdminDTO(domain.getId(), domain.getUsername());
    }

    public static Result<Admin> toDomain(com.eventer.admin.data.model.Admin model) {
        return Admin.create(
                model.getId(), model.getUsername(), model.getPassword(), model.getRole());
    }

    public static com.eventer.admin.data.model.Admin toModel(Admin domain) {
        var model = new com.eventer.admin.data.model.Admin();

        model.setId(domain.getId());
        model.setUsername(domain.getUsername());
        model.setPassword(domain.getPassword());
        model.setRole(domain.getRole().getName());

        return model;
    }
}
