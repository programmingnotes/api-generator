package io.programmingnotes.apigenerator.config;

import lombok.Getter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;

@Configuration
public class JwtConfig {
    @Value("${security.jwt.secret}")
    @Getter
    private String secret;

    @Value("${security.jwt.expirationInMs}")
    @Getter
    private Long expirationInMs;


}