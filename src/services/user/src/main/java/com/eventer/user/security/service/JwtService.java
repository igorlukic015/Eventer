package com.eventer.user.security.service;

import org.springframework.security.core.userdetails.UserDetails;

import java.util.Date;
import java.util.Map;
import java.util.function.Function;

import io.jsonwebtoken.Claims;

import javax.crypto.SecretKey;

public interface JwtService {
    String extractUsername(String token);

    Date extractExpiration(String token);

    <T> T extractClaim(String token, Function<Claims, T> claimsResolver);

    Claims extractClaims(String token);

    Boolean isTokenExpired(String token);

    Boolean validateToken(String token, UserDetails userDetails);

    String generateToken(String username);

    String generateServiceToken();

    String createToken(Map<String, Object> claims, String username, SecretKey secretKey);
}
