//package com.userservice.security;
//
//import com.userservice.model.User;
//import io.jsonwebtoken.Claims;
//import io.jsonwebtoken.Jwts;
//import io.jsonwebtoken.SignatureAlgorithm;
//import io.jsonwebtoken.security.Keys;
//import org.springframework.stereotype.Service;
//
//import java.nio.charset.StandardCharsets;
//import java.security.Key;
//import java.util.Date;
//
//@Service
//public class JwtService {
//    private static final String SECRET_KEY = "my-very-long-and-secure-jwt-secret-key-123456";
//
//    private Key getSigningKey() {
//        return Keys.hmacShaKeyFor(SECRET_KEY.getBytes(StandardCharsets.UTF_8));
//    }
//
//    public String generateToken(User user) {
//        return Jwts.builder()
//                .setSubject(user.getEmail())
//                .claim("userId", user.getId())
//                .setIssuedAt(new Date())
//                .setExpiration(new Date(System.currentTimeMillis() + 86400000)) // 1 ngày
//                .signWith(getSigningKey(), SignatureAlgorithm.HS256)
//                .compact();
//    }
//
//    public Claims extractClaims(String token) {
//        return Jwts.parser()
//                .setSigningKey(getSigningKey())
//                .build()
//                .parseClaimsJws(token)
//                .getBody();
//    }
//
//    public boolean isTokenValid(String token) {
//        return extractClaims(token).getExpiration().after(new Date());
//    }
//
//    public Long extractUserId(String token) {
//        return extractClaims(token).get("userId", Long.class);
//    }
//
//    public String extractEmail(String token) {
//        return extractClaims(token).getSubject();
//    }
//}
