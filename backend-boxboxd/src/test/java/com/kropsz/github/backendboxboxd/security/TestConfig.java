package com.kropsz.github.backendboxboxd.security;

import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.security.oauth2.jwt.JwtEncoder;

@TestConfiguration
public class TestConfig {
    @Bean
    public JwtTokenMock jwtTokenMock(JwtEncoder jwtEncoder) {
        return new JwtTokenMock(jwtEncoder);
    }
}
