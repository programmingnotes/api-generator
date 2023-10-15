package io.programmingnotes.apigenerator.service;

import io.jsonwebtoken.Jwts;
import io.programmingnotes.apigenerator.config.JwtConfig;
import org.springframework.stereotype.Service;

import java.util.Date;
import io.jsonwebtoken.SignatureAlgorithm;


@Service
public class JwtTokenProvider {
    private final JwtConfig jwtConfig;

    public JwtTokenProvider(JwtConfig jwtConfig) {
        this.jwtConfig = jwtConfig;
    }

    public String generateToken(String username) {
        Date now = new Date();
        Date expiryDate = new Date(now.getTime() + jwtConfig.getExpirationInMs());

        return Jwts.builder()
                .setSubject(username)
                .setIssuedAt(now)
                .setExpiration(expiryDate)
                .signWith(SignatureAlgorithm.HS256, jwtConfig.getSecret())
                .compact();
    }
}

