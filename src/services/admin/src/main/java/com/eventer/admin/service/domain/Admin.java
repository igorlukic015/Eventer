package com.eventer.admin.service.domain;

import com.github.igorlukic015.resulter.Result;
import com.eventer.admin.utils.ResultErrorMessages;
import org.springframework.util.StringUtils;

public class Admin {
    private final Long id;

    private final String username;

    private final String password;

    private final Role role;

    private Admin(Long id, String username, String password, Role role) {
        this.id = id;
        this.username = username;
        this.password = password;
        this.role = role;
    }

    public static Result<Admin> create(Long id, String username, String password, String role) {
        Result<Role> roleOrError = Role.create(role);

        if (roleOrError.isFailure()) {
            return Result.fromError(roleOrError);
        }

        Admin admin = new Admin(id, username, password, roleOrError.getValue());

        return Result.success(admin);
    }

    public static Result<Admin> create(String username, String password, Role role) {
        if (!StringUtils.hasText(username) || username.length() > 255) {
            return Result.invalid(ResultErrorMessages.invalidAdminUsername);
        }

        if (!StringUtils.hasText(password) || password.length() < 6 || password.length() > 255) {
            return Result.invalid(ResultErrorMessages.invalidPassword);
        }

        Admin admin = new Admin(null, username, password, role);

        return Result.success(admin);
    }

    public Long getId() {
        return id;
    }

    public String getUsername() {
        return username;
    }

    public String getPassword() {
        return password;
    }

    public Role getRole() {
        return role;
    }
}
