package com.eventer.user.security.service.impl;

import com.eventer.user.security.contracts.AuthorityConstants;
import com.eventer.user.security.service.JwtService;
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
    public String extractUsername(String token) {
        return extractClaim(token, Claims::getSubject);
    }

    @Override
    public Date extractExpiration(String token) {
        return extractClaim(token, Claims::getExpiration);
    }

    @Override
    public <T> T extractClaim(String token, Function<Claims, T> claimsResolver) {
        final Claims claims = extractClaims(token);
        return claimsResolver.apply(claims);
    }

    @Override
    public Claims extractClaims(String token) {
        return Jwts.parser().verifyWith(getSecretKey()).build().parseSignedClaims(token).getPayload();
    }

    @Override
    public Boolean isTokenExpired(String token) {
        return extractExpiration(token).before(new Date());
    }

    @Override
    public Boolean validateToken(String token, UserDetails userDetails) {
        final String username = extractUsername(token);
        return (username.equals(userDetails.getUsername()) && !isTokenExpired(token));
    }

    @Override
    public String generateToken(String username) {
        Map<String, Object> claims = new HashMap<>();
        return createToken(claims, username, getSecretKey());
    }

    @Override
    public String generateServiceToken() {
        Map<String, Object> claims = new HashMap<>();
        return createToken(claims, AuthorityConstants.USER_SERVICE, getServiceSecretKey());
    }

    @Override
    public String createToken(Map<String, Object> claims, String username, SecretKey secretKey) {
        return Jwts.builder()
                .claims(claims)
                .subject(username)
                .issuedAt(new Date(System.currentTimeMillis()))
                .expiration(new Date(System.currentTimeMillis() + this.expiryInMillis))
                .signWith(secretKey)
                .compact();
    }

    private SecretKey getSecretKey() {
        return Keys.hmacShaKeyFor(Decoders.BASE64.decode(this.secret));
    }

    private SecretKey getServiceSecretKey() {
        return Keys.hmacShaKeyFor(Decoders.BASE64.decode(this.serviceSecret));
    }
}
