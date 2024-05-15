package com.kropsz.github.backendboxboxd.util;

import com.kropsz.github.backendboxboxd.entities.User;
import com.kropsz.github.backendboxboxd.exception.BusinessViolationException;
import com.kropsz.github.backendboxboxd.repository.UserRepository;
import com.kropsz.github.backendboxboxd.web.dtos.LoginResponse;
import com.kropsz.github.backendboxboxd.web.dtos.UserLoginDto;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.oauth2.jwt.JwtClaimsSet;
import org.springframework.security.oauth2.jwt.JwtEncoder;
import org.springframework.security.oauth2.jwt.JwtEncoderParameters;
import org.springframework.stereotype.Component;

import java.time.Instant;
import java.util.Optional;

@Component
@RequiredArgsConstructor
public class SessionVerification {

    private final UserRepository userRepository;
    private final JwtEncoder jwtEncoder;

    public void verifyIfCredentialsExists(User user) {
        if (userRepository.existsByUsernameOrEmail(user.getUsername(), user.getEmail())) {
            throw new BusinessViolationException("Email ou Username ja existem");
        }
    }

    public boolean checkPasswordIsEqual(String password, String passwordEncode) {
        return new BCryptPasswordEncoder().matches(password, passwordEncode);
    }

    public void verfiyLogin(Optional<User> user, UserLoginDto login) {
        if (user.isEmpty() || !checkPasswordIsEqual(login.password(), user.get().getPassword())) {
            throw new BadCredentialsException("Usuário não existe ou senha errada");
        }
    }

    public LoginResponse createToken(Optional<User> user) {
        var now = Instant.now();
        var expiresIn = 300L;
        if (user.isPresent()) {
            var claims = JwtClaimsSet.builder()
                    .issuer("BackEnd")
                    .subject(user.get().getId().toString())
                    .issuedAt(now)
                    .expiresAt(now.plusSeconds(expiresIn)).build();

            var jwtValue = jwtEncoder.encode(JwtEncoderParameters.from(claims)).getTokenValue();

            return new LoginResponse(jwtValue, expiresIn);
        }
        return null;
    }

}
