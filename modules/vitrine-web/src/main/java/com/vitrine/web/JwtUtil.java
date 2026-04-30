package com.vitrine.web;

import com.vitrine.api.model.Customer;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;

import javax.crypto.SecretKey;
import java.nio.charset.StandardCharsets;
import java.util.Date;

public class JwtUtil {

    private static final String SECRET = "vitrine-super-secret-key#$^!1543!-that-is9182u!@1241f!@#12312long-enough-256bits!!";
    private static final long EXPIRATION_MS = 1000 * 60 * 60; // 1 hour

    private static final SecretKey KEY = Keys.hmacShaKeyFor(SECRET.getBytes(StandardCharsets.UTF_8));

    public static String generateToken(Customer customer) {
        return Jwts.builder()
                .subject(String.valueOf(customer.getId()))
                .claim("email", customer.getEmail())
                .issuedAt(new Date())
                .expiration(new Date(System.currentTimeMillis() + EXPIRATION_MS))
                .signWith(KEY)
                .compact();
    }

    public static Claims validateToken(String token) {
        return Jwts.parser()
                .verifyWith(KEY)
                .build()
                .parseSignedClaims(token)
                .getPayload();
    }
}
