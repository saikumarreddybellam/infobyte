//package com.infobyte.task.project.services;
//
//import io.jsonwebtoken.JwtException;
//import io.jsonwebtoken.Jwts;
//import io.jsonwebtoken.security.Keys;
//import org.springframework.beans.factory.annotation.Value;
//import org.springframework.stereotype.Component;
//
//@Component
//public class JwtTokenService {
//    @Value("${app.jwt.secret}")
//    private String secret;
//
//    @Value("${app.jwt.expiration}")
//    private long expirationMs;
//
//    // Add token invalidation check
//    public boolean isTokenValid(String token) {
//        try {
//            Jwts.parserBuilder()
//                    .setSigningKey(Keys.hmacShaKeyFor(secret.getBytes()))
//                    .build()
//                    .parseClaimsJws(token);
//            return true;
//        } catch (JwtException | IllegalArgumentException e) {
//            return false;
//        }
//    }
//
//    // Add more specific exceptions
//    public String extractUsername(String token) {
//        try {
//            return Jwts.parserBuilder()
//                    .setSigningKey(Keys.hmacShaKeyFor(secret.getBytes()))
//                    .build()
//                    .parseClaimsJws(token)
//                    .getBody()
//                    .getSubject();
//        } catch (JwtException | IllegalArgumentException e) {
//            throw new RuntimeException("Invalid JWT token", e);
//        }
//    }
//}