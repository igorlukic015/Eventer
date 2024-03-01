package com.eventer.admin.security.service;

import io.jsonwebtoken.Claims;
import org.springframework.security.core.userdetails.UserDetails;

import javax.crypto.SecretKey;
import java.util.Date;
import java.util.Map;
import java.util.function.Function;

public interface JwtService {
    String extractUsername(String token, boolean isService);// 1

    Date extractExpiration(String token, SecretKey secretKey);

    <T> T extractClaim(String token, Function<Claims, T> claimsResolver, SecretKey secretKey);

    Claims extractClaims(String token, SecretKey secretKey);

    Boolean isTokenExpired(String token, SecretKey secretKey);

    Boolean validateToken(String token, UserDetails userDetails);

    String generateToken(String username);

    String createToken(Map<String, Object> claims, String username);

    boolean validateTokenFromService(String token);
}
