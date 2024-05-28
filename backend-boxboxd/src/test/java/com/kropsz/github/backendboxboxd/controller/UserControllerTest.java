package com.kropsz.github.backendboxboxd.controller;

import static com.kropsz.github.backendboxboxd.common.UserConstants.EXISTING_USER;
import static com.kropsz.github.backendboxboxd.common.UserConstants.INVALID_REGISTER;
import static com.kropsz.github.backendboxboxd.common.UserConstants.VALID_REGISTER;
import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.web.reactive.server.WebTestClient;

import com.kropsz.github.backendboxboxd.web.dtos.LoginResponse;
import com.kropsz.github.backendboxboxd.web.dtos.UserLoginDto;
import com.kropsz.github.backendboxboxd.web.dtos.UserResponse;
import com.kropsz.github.backendboxboxd.web.exceptions.ErrorMessage;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@Sql(scripts = "/database/users/data.sql", executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
@Sql(scripts = "/database/users/delete-data.sql", executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
@Sql(scripts = "/database/users/user.sql", executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
@Sql(scripts = "/database/users/delete-user.sql", executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
class UserControllerTest {

    @Autowired
    WebTestClient testClient;



    @Test
    @DisplayName("Register user with success")
    void registerUser_WithSuccess() {
        UserResponse responseBody = testClient.post()
                .uri("/api/boxboxd/register")
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(VALID_REGISTER)
                .exchange()
                .expectStatus().isCreated()
                .expectBody(UserResponse.class)
                .returnResult().getResponseBody();

        assertThat(responseBody).isNotNull();
        assertThat(responseBody.getUsername()).isEqualTo("pedrinho");
        assertThat(responseBody.getEmail()).isEqualTo("pedro@example.com");

    }

    @Test
    @DisplayName("Register user with username or email already registered")
    void registerUser_WithUsernameOrEmailAlreadyRegistered() {
        ErrorMessage responseBody = testClient.post()
                .uri("/api/boxboxd/register")
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(EXISTING_USER)
                .exchange()
                .expectStatus().isEqualTo(409)
                .expectBody(ErrorMessage.class)
                .returnResult().getResponseBody();

        assertThat(responseBody).isNotNull();
        assertThat(responseBody.getMessage()).isEqualTo("Email ou Username ja existem");
        assertThat(responseBody.getStatus()).isEqualTo(409);

    }

    @Test
    @DisplayName("Register user with invalid data")
    void registerUser_WithInvalidData() {
        ErrorMessage responseBody = testClient.post()
                .uri("/api/boxboxd/register")
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(INVALID_REGISTER)
                .exchange()
                .expectStatus().isEqualTo(422)
                .expectBody(ErrorMessage.class)
                .returnResult().getResponseBody();

        assertThat(responseBody).isNotNull();
        assertThat(responseBody.getMessage()).isEqualTo("Campos Inválidos: ");
        assertThat(responseBody.getStatus()).isEqualTo(422);
    }

    @Test
    @DisplayName("Login user with success")
    void loginUser_WithSuccess() {
        LoginResponse responseBody = testClient.post()
                .uri("/api/boxboxd/login")
                .accept(MediaType.APPLICATION_JSON)
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(new UserLoginDto("johndoe", "password"))
                .exchange()
                .expectStatus().isOk()
                .expectBody(LoginResponse.class)
                .returnResult().getResponseBody();

        assertThat(responseBody).isNotNull();
        assertThat(responseBody.token()).isNotNull();
        assertThat(responseBody.expiresIn()).isEqualTo(300L);

    }

    @Test
    @DisplayName("Login user with invalid credentials")
    void loginUser_WithInvalidCredentials() {
        ErrorMessage responseBody = testClient.post()
                .uri("/api/boxboxd/login")
                .accept(MediaType.APPLICATION_JSON)
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(new UserLoginDto("johndoe", "invalidpassword"))
                .exchange()
                .expectStatus().isBadRequest()
                .expectBody(ErrorMessage.class)
                .returnResult().getResponseBody();

        assertThat(responseBody).isNotNull();
        assertThat(responseBody.getMessage()).isEqualTo("Usuário não existe ou senha errada");
        assertThat(responseBody.getStatus()).isEqualTo(400);

    }
}
