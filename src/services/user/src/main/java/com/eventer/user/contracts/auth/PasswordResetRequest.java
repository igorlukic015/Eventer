package com.eventer.user.contracts.auth;

public record PasswordResetRequest(String email, String password, String code)  {}
