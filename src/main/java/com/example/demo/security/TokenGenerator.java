package com.example.demo.security;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.example.demo.entity.User;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneOffset;

@Service
public class TokenGenerator {
    @Value("${jwt_key}")
    String validation;

    public String generateToken(User user) {
        Algorithm algorithm = Algorithm.HMAC256(validation);
        String token = JWT.create().withIssuer("auth_api").withSubject(user.getName()).withExpiresAt(generateExpirationDate()).sign(algorithm);
        return token;
    }
    public String validateToken(String token) {
        Algorithm algorithm = Algorithm.HMAC256(validation);
        return JWT.require(algorithm).withIssuer("auth_api").build().verify(token).getSubject();
    }

    private Instant generateExpirationDate() {
        return LocalDateTime.now().plusHours(2).toInstant(ZoneOffset.UTC);
    }
}
