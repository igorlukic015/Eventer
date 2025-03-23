package com.eventer.user.contracts.auth;

public record RegisterRequest(String name, String username, String password, String city) {}
