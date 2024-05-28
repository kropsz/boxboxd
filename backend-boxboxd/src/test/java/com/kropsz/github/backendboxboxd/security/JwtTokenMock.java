package com.kropsz.github.backendboxboxd.security;

import java.time.Instant;
import java.util.Collections;

import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.security.oauth2.jwt.JwtClaimsSet;
import org.springframework.security.oauth2.jwt.JwtEncoder;
import org.springframework.security.oauth2.jwt.JwtEncoderParameters;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationToken;

import com.kropsz.github.backendboxboxd.entities.User;

public class JwtTokenMock {

    private JwtEncoder jwtEncoder;

    public JwtTokenMock(JwtEncoder jwtEncoder) {
        this.jwtEncoder = jwtEncoder;
    }

    public JwtAuthenticationToken createMockToken() {
        var mockUser = new User();
        mockUser.setId(1L); // ID arbitrário

        var now = Instant.now();
        var expiresIn = 300L;

        var claims = JwtClaimsSet.builder()
                .issuer("BackEnd")
                .subject(mockUser.getId().toString())
                .issuedAt(now)
                .expiresAt(now.plusSeconds(expiresIn)).build();

        var jwtValue = jwtEncoder.encode(JwtEncoderParameters.from(claims)).getTokenValue();

        Jwt jwt = Jwt.withTokenValue(jwtValue)
                .header("typ", "JWT")
                .claims(claimsMap -> claimsMap.putAll(claims.getClaims()))
                .build();

        return new JwtAuthenticationToken(jwt, Collections.emptyList());
    }

}
