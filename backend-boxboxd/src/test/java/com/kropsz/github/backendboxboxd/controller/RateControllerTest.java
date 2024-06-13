package com.kropsz.github.backendboxboxd.controller;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.web.reactive.server.WebTestClient;

import com.kropsz.github.backendboxboxd.security.JwtTokenMock;
import com.kropsz.github.backendboxboxd.security.TestConfig;
import com.kropsz.github.backendboxboxd.web.exceptions.ErrorMessage;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@Sql(scripts = "/database/rate/rate.sql", executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
@Sql(scripts = "/database/rate/delete-rate.sql", executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
@Import(TestConfig.class)
class RateControllerTest {
    
    @Autowired
    WebTestClient testClient;

    @Autowired
    JwtTokenMock jwtTokenMock;

    @Test
    @DisplayName("Add rate sucessfully")
    void addRateToDriver() {
        testClient.post()
                .uri("/api/boxboxd/rate/driver/VER/5")
                .header("Authorization", "Bearer " + jwtTokenMock.createMockToken().getToken().getTokenValue())
                .exchange()
                .expectStatus().isOk();

        testClient.post()
                .uri("/api/boxboxd/rate/circuit/silverstone/5")
                .header("Authorization", "Bearer " + jwtTokenMock.createMockToken().getToken().getTokenValue())
                .exchange()
                .expectStatus().isOk();
    }

    @Test
    @DisplayName("Add rate already exists")
    void addRateAlreadyExists() {
        ErrorMessage result = testClient.post()
                .uri("/api/boxboxd/rate/driver/HAM/5")
                .header("Authorization", "Bearer " + jwtTokenMock.createMockToken().getToken().getTokenValue())
                .exchange()
                .expectStatus().isEqualTo(409)
                .expectBody(ErrorMessage.class)
                .returnResult().getResponseBody();

        assertThat(result.getMessage()).isEqualTo("Avaliação já existe");
        assertThat(result.getStatus()).isEqualTo(409);
        assertThat(result.getStatusText()).isEqualTo("Conflict");
    }

    @Test
    @DisplayName("Add rate invalid type")
    void addRateInvalidEntity() {
        ErrorMessage result = testClient.post()
                .uri("/api/boxboxd/rate/invalid/VER/5")
                .header("Authorization", "Bearer " + jwtTokenMock.createMockToken().getToken().getTokenValue())
                .exchange()
                .expectStatus().is5xxServerError()
                .expectBody(ErrorMessage.class)
                .returnResult().getResponseBody();

        assertThat(result.getStatus()).isEqualTo(500);
        assertThat(result.getStatusText()).isEqualTo("Internal Server Error");
    }

    @Test
    @DisplayName("Add rate invalid entity id")
    void addRateInvalidEntityId() {
        ErrorMessage result = testClient.post()
                .uri("/api/boxboxd/rate/driver/invalid/5")
                .header("Authorization", "Bearer " + jwtTokenMock.createMockToken().getToken().getTokenValue())
                .exchange()
                .expectStatus().isNotFound()
                .expectBody(ErrorMessage.class)
                .returnResult().getResponseBody();

        assertThat(result.getStatus()).isEqualTo(404);
        assertThat(result.getStatusText()).isEqualTo("Not Found");
        assertThat(result.getMessage()).isEqualTo("Piloto não encontrado");
    }

    @Test
    @DisplayName("Delete rate sucessfully")
    void deleteRate() {
        testClient.delete()
                .uri("/api/boxboxd/rate/driver/HAM")
                .header("Authorization", "Bearer " + jwtTokenMock.createMockToken().getToken().getTokenValue())
                .exchange()
                .expectStatus().isOk();
    }

    @Test
    @DisplayName("Delete rate not found")
    void deleteRateNotFound() {
        ErrorMessage result = testClient.delete()
                .uri("/api/boxboxd/rate/driver/VER")
                .header("Authorization", "Bearer " + jwtTokenMock.createMockToken().getToken().getTokenValue())
                .exchange()
                .expectStatus().isNotFound()
                .expectBody(ErrorMessage.class)
                .returnResult().getResponseBody();

        assertThat(result.getStatus()).isEqualTo(404);
        assertThat(result.getStatusText()).isEqualTo("Not Found");
        assertThat(result.getMessage()).isEqualTo("Avaliação não encontrada");
    }

    @Test
    @DisplayName("Update rate sucessfully")
    void updateRate() {
        testClient.put()
                .uri("/api/boxboxd/rate/driver/HAM/4")
                .header("Authorization", "Bearer " + jwtTokenMock.createMockToken().getToken().getTokenValue())
                .exchange()
                .expectStatus().isOk();
    }

    @Test
    @DisplayName("Update rate not found")
    void updateRateNotFound() {
        ErrorMessage result = testClient.put()
                .uri("/api/boxboxd/rate/driver/VER/4")
                .header("Authorization", "Bearer " + jwtTokenMock.createMockToken().getToken().getTokenValue())
                .exchange()
                .expectStatus().isNotFound()
                .expectBody(ErrorMessage.class)
                .returnResult().getResponseBody();

        assertThat(result.getStatus()).isEqualTo(404);
        assertThat(result.getStatusText()).isEqualTo("Not Found");
        assertThat(result.getMessage()).isEqualTo("Avaliação não encontrada");
    }
}
