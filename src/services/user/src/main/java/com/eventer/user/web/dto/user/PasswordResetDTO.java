package com.eventer.user.web.dto.user;

public record PasswordResetDTO(String email, String password, String code) {}
