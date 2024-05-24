package com.eventer.user.contracts.auth;

public record UpdateProfileRequest(Long id, String username, String name, String city) {}
