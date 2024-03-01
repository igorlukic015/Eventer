package com.eventer.admin.security.service.impl;

import com.eventer.admin.security.service.JwtService;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import javax.crypto.SecretKey;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;

@Service
public class JwtServiceImpl implements JwtService {

    @Value("${expiry-in-millis}")
    private int expiryInMillis;

    @Value("${secret}")
    private String secret;

    @Value("${service-secret}")
    private String serviceSecret;

    @Override
    public String extractUsername(String token, boolean isService) {
        SecretKey secretKey = isService ? getServiceSecretKey() : getSecretKey();

        return extractClaim(token, Claims::getSubject, secretKey);
    }

    @Override
    public Date extractExpiration(String token, SecretKey secretKey) {
        return extractClaim(token, Claims::getExpiration, secretKey);
    }

    @Override
    public <T> T extractClaim(String token, Function<Claims, T> claimsResolver, SecretKey secretKey) {
        final Claims claims = extractClaims(token, secretKey);
        return claimsResolver.apply(claims);
    }

    @Override
    public Claims extractClaims(String token, SecretKey secretKey) {
        return Jwts.parser()
                .verifyWith(secretKey)
                .build()
                .parseSignedClaims(token)
                .getPayload();
    }

    @Override
    public Boolean isTokenExpired(String token, SecretKey secretKey) {
        return extractExpiration(token, secretKey).before(new Date());
    }

    @Override
    public Boolean validateToken(String token, UserDetails userDetails) {
        final String username = extractUsername(token, false);
        return (username.equals(userDetails.getUsername()) && !isTokenExpired(token, getSecretKey()));
    }

    @Override
    public boolean validateTokenFromService(String token) {
        return !isTokenExpired(token, getServiceSecretKey());
    }

    @Override
    public String generateToken(String username) {
        Map<String, Object> claims = new HashMap<>();
        return createToken(claims, username);
    }

    @Override
    public String createToken(Map<String, Object> claims, String username) {
        return Jwts.builder()
                .claims(claims)
                .subject(username)
                .issuedAt(new Date(System.currentTimeMillis()))
                .expiration(new Date(System.currentTimeMillis() + this.expiryInMillis))
                .signWith(getSecretKey())
                .compact();
    }

    private SecretKey getSecretKey() {
        return Keys.hmacShaKeyFor(
                Decoders.BASE64.decode(this.secret));
    }

    private SecretKey getServiceSecretKey() {
        return Keys.hmacShaKeyFor(
                Decoders.BASE64.decode(this.serviceSecret));
    }
}
