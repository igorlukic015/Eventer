package com.eventer.admin.mapper;

import com.eventer.admin.contracts.auth.AuthenticationResponse;
import com.eventer.admin.contracts.auth.LoginRequest;
import com.eventer.admin.contracts.auth.RegisterRequest;
import com.eventer.admin.service.domain.Admin;
import com.github.igorlukic015.resulter.Result;
import com.eventer.admin.web.dto.admin.AdminDTO;
import com.eventer.admin.web.dto.auth.AuthenticationResponseDTO;
import com.eventer.admin.web.dto.auth.LoginDTO;
import com.eventer.admin.web.dto.auth.RegisterDTO;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;

import java.util.List;
import java.util.stream.Collectors;

public class AdminMapper {

    public static LoginRequest toRequest(LoginDTO dto) {
        return new LoginRequest(dto.username(), dto.password());
    }

    public static AuthenticationResponseDTO toDTO(AuthenticationResponse response) {
        return new AuthenticationResponseDTO(response.accessToken(), response.role());
    }

    public static RegisterRequest toRequest(RegisterDTO dto) {
        return new RegisterRequest(dto.username(), dto.password());
    }

    public static AdminDTO toDTO(Admin domain) {
        return new AdminDTO(domain.getId(), domain.getUsername());
    }

    public static Page<AdminDTO> toDTOs(Page<Admin> admins) {
        List<AdminDTO> dtos = admins.stream().map(AdminMapper::toDTO).toList();

        return new PageImpl<>(dtos, admins.getPageable(), admins.getTotalElements());
    }

    public static Result<Admin> toDomain(com.eventer.admin.data.model.Admin model) {
        return Admin.create(
                model.getId(), model.getUsername(), model.getPassword(), model.getRole());
    }

    public static Result<Page<Admin>> toDomainPage(
            Page<com.eventer.admin.data.model.Admin> foundAdmins) {
        Result<List<Admin>> adminsOrError =
                Result.getResultValueSet(
                        foundAdmins.stream().map(AdminMapper::toDomain).toList(),
                        Collectors.toList());

        if (adminsOrError.isFailure()) {
            return Result.fromError(adminsOrError);
        }

        Page<Admin> result =
                new PageImpl<>(
                        adminsOrError.getValue(),
                        foundAdmins.getPageable(),
                        foundAdmins.getTotalElements());

        return Result.success(result);
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
