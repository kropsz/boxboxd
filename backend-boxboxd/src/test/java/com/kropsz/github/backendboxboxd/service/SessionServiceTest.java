package com.kropsz.github.backendboxboxd.service;

import static com.kropsz.github.backendboxboxd.common.UserConstants.VALID_LOGIN;
import static com.kropsz.github.backendboxboxd.common.UserConstants.VALID_USER;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doAnswer;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.Optional;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.mockito.junit.jupiter.MockitoSettings;
import org.mockito.quality.Strictness;
import org.springframework.security.authentication.BadCredentialsException;

import com.kropsz.github.backendboxboxd.entities.User;
import com.kropsz.github.backendboxboxd.exception.BusinessViolationException;
import com.kropsz.github.backendboxboxd.repository.UserRepository;
import com.kropsz.github.backendboxboxd.util.SessionVerification;
import com.kropsz.github.backendboxboxd.web.dtos.LoginResponse;
import com.kropsz.github.backendboxboxd.web.dtos.UserLoginDto;

@ExtendWith(MockitoExtension.class)
@MockitoSettings(strictness = Strictness.LENIENT)
 class SessionServiceTest {

    @InjectMocks
    private SessionService sessionService;

    @Mock
    private UserRepository userRepository;

    @Mock
    private SessionVerification sessionVerification;

    @Test
    @DisplayName("Register new user sucessfull")
    void registerUser_Sucessfull(){
        doAnswer(invocation -> {
            User userArgument = invocation.getArgument(0);
            if (userRepository.existsByUsernameOrEmail(userArgument.getUsername(), userArgument.getEmail())) {
                throw new BusinessViolationException("Email ou Username já existem");
            }
            return null;
        }).when(sessionVerification).verifyIfCredentialsExists(any(User.class));
        when(userRepository.save(any(User.class))).thenReturn(VALID_USER);
        User registeredUser = sessionService.registerUser(VALID_USER);
        verify(userRepository).save(VALID_USER);

        assertEquals(VALID_USER, registeredUser);
    }

    @Test
    @DisplayName("Register new user with duplicate credentials")
    void registerUser_DuplicateCredentials() {
        doThrow(new BusinessViolationException("Email ou Username já existem")).when(sessionVerification).verifyIfCredentialsExists(any(User.class));
        assertThatThrownBy(() -> sessionService.registerUser(VALID_USER)).isInstanceOf(BusinessViolationException.class);
    }

    @SuppressWarnings("unchecked")
    @Test
    @DisplayName("Login sucessfull")
    void testMakeLogin_Success() {
        when(userRepository.findByUsername(VALID_LOGIN.username())).thenReturn(Optional.of(VALID_USER));
        doNothing().when(sessionVerification).verfiyLogin(any(Optional.class), any(UserLoginDto.class));
        LoginResponse expectedResponse = new LoginResponse("token", 300L);
        when(sessionVerification.createToken(any(Optional.class))).thenReturn(expectedResponse);
        LoginResponse loginResponse = sessionService.makeLogin(VALID_LOGIN);

        verify(userRepository).findByUsername(VALID_LOGIN.username());
        verify(sessionVerification).verfiyLogin(Optional.of(VALID_USER), VALID_LOGIN);
        verify(sessionVerification).createToken(Optional.of(VALID_USER));

        assertEquals(expectedResponse, loginResponse);
    }

    @Test
    @DisplayName("Login invalid credentials")
    void testMakeLogin_InvalidData() {
        when(userRepository.findByUsername(VALID_LOGIN.username())).thenReturn(Optional.empty());
        doThrow(new BadCredentialsException("Usuário não existe ou senha errada"))
                .when(sessionVerification).verfiyLogin(Optional.empty(), VALID_LOGIN);
        assertThatThrownBy(() -> sessionService.makeLogin(VALID_LOGIN)).isInstanceOf(BadCredentialsException.class);
    }
}

