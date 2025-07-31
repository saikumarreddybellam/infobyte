package com.infobyte.task.project.utility;

import com.infobyte.task.project.entities.User;
import com.infobyte.task.project.exceptions.JwtAuthenticationException;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.MalformedJwtException;
import io.jsonwebtoken.UnsupportedJwtException;
import io.jsonwebtoken.security.Keys;
import io.jsonwebtoken.security.SignatureException;
import jakarta.annotation.PostConstruct;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.security.Key;
import java.util.Date;

@Slf4j
@Component
public class JwtTokenService {

    private final JwtConfig jwtConfig;
    private Key key;

    public JwtTokenService(JwtConfig jwtConfig) {
        this.jwtConfig = jwtConfig;
    }

    @PostConstruct
    public void init() {
        String secret = jwtConfig.getSecret();
        if (secret == null || secret.isEmpty()) {
            throw new IllegalStateException("JWT secret key is not configured");
        }
        this.key = Keys.hmacShaKeyFor(secret.getBytes());
        log.info("JWT Token Service initialized with secret key");
    }


    public String generateToken(User user) {
        if (key == null) {
            throw new IllegalStateException("JWT key not initialized");
        }
        return Jwts.builder()
                .setSubject(user.getUsername())
                .claim("role", "ROLE_" + user.getRole())  // Ensure role has ROLE_ prefix
                .setIssuedAt(new Date())
                .setExpiration(new Date(System.currentTimeMillis() + jwtConfig.getExpiration()))
                .signWith(key)
                .compact();
    }

    public boolean isTokenValid(String token) {
        try {
            Jwts.parserBuilder()
                    .setSigningKey(key)
                    .build()
                    .parseClaimsJws(token);
            return true;
        } catch (Exception e) {
            log.error("Invalid JWT token: {}", e.getMessage());
            return false;
        }
    }

    public String extractUsername(String jwt) {
        try {
            return Jwts.parserBuilder()
                    .setSigningKey(key)
                    .build()
                    .parseClaimsJws(jwt)
                    .getBody()
                    .getSubject();
        } catch (ExpiredJwtException e) {
            log.warn("JWT token expired: {}", e.getMessage());
            throw new JwtAuthenticationException("Token expired", e);
        } catch (UnsupportedJwtException e) {
            log.warn("Unsupported JWT token: {}", e.getMessage());
            throw new JwtAuthenticationException("Unsupported token", e);
        } catch (MalformedJwtException e) {
            log.warn("Invalid JWT token: {}", e.getMessage());
            throw new JwtAuthenticationException("Invalid token", e);
        } catch (SignatureException e) {
            log.warn("JWT signature validation failed: {}", e.getMessage());
            throw new JwtAuthenticationException("Signature validation failed", e);
        } catch (IllegalArgumentException e) {
            log.warn("JWT claims string is empty: {}", e.getMessage());
            throw new JwtAuthenticationException("Token claims empty", e);
        }
    }
}
