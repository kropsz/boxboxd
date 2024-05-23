package com.kropsz.github.backendboxboxd.service;

import com.kropsz.github.backendboxboxd.entities.User;
import com.kropsz.github.backendboxboxd.repository.UserRepository;
import com.kropsz.github.backendboxboxd.util.SessionVerification;
import com.kropsz.github.backendboxboxd.web.dtos.LoginResponse;
import com.kropsz.github.backendboxboxd.web.dtos.UserLoginDto;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class SessionService {

    private final UserRepository userRepository;
    private final SessionVerification sessionVerification;

    @Transactional
    public User registerUser(User user) {
        sessionVerification.verifyIfCredentialsExists(user);
        user.setPassword(encryptPassword(user.getPassword()));
        return userRepository.save(user);
    }

    @Transactional
    public LoginResponse makeLogin(UserLoginDto loginDto){
        Optional<User> optionalUser = userRepository.findByUsername(loginDto.username());
        sessionVerification.verfiyLogin(optionalUser, loginDto);
        return sessionVerification.createToken(optionalUser);
    }


    public String encryptPassword(String password) {
        return new BCryptPasswordEncoder().encode(password);
    }
}
