package com.demosecurity.security;

import com.demosecurity.model.User;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import org.springframework.stereotype.Component;

import java.nio.charset.StandardCharsets;
import java.security.Key;
import java.util.Date;

@Component
public class JwtUtil {
    private final String SECRET_KEY = "my-very-long-and-secure-jwt-secret-key-123456";

    private Key getSigningKey() {
        return Keys.hmacShaKeyFor(SECRET_KEY.getBytes(StandardCharsets.UTF_8));
    }

    public String generateToken(User user) {
        System.out.println("generateToken");
        return Jwts.builder()
                .setSubject(user.getUsername())
                .claim("userId", user.getId())
                .setIssuedAt(new Date())
                .setExpiration(new Date(System.currentTimeMillis() + 86400000))
                .signWith(SignatureAlgorithm.HS256, getSigningKey())
                .compact();
    }

    public String extractUsername(String token) {
        System.out.println("extractUsername");
        return extractClaims(token).getSubject();
    }

    public boolean validateToken(String token) {
        System.out.println("validateToken");
        return extractClaims(token).getExpiration().after(new Date());
    }

    public Claims extractClaims(String token) {
        System.out.println("extractClaims");
        return Jwts.parser()
                .setSigningKey(getSigningKey())
                .build()
                .parseClaimsJws(token)
                .getBody();
    }
}
