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
@Sql(scripts = "/database/likes/likes.sql", executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
@Sql(scripts = "/database/likes/delete-likes.sql", executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
@Import(TestConfig.class)
class LikeControllerTest {

    @Autowired
    WebTestClient testClient;

    @Autowired
    JwtTokenMock jwtTokenMock;

    @Test
    @DisplayName("Add like sucessfully")
    void addLikeToDriver() {
        testClient.post()
                .uri("/api/boxboxd/like/driver/VER")
                .header("Authorization", "Bearer " + jwtTokenMock.createMockToken().getToken().getTokenValue())
                .exchange()
                .expectStatus().isOk();

        testClient.post()
                .uri("/api/boxboxd/like/team/Ferrari")
                .header("Authorization", "Bearer " + jwtTokenMock.createMockToken().getToken().getTokenValue())
                .exchange()
                .expectStatus().isOk();

        testClient.post()
                .uri("/api/boxboxd/like/circuit/silverstone")
                .header("Authorization", "Bearer " + jwtTokenMock.createMockToken().getToken().getTokenValue())
                .exchange()
                .expectStatus().isOk();
    }

    @Test
    @DisplayName("Add like to driver, team, circuit does not exist")
    void addLikeToEntityThatDoesNotExist() {
        ErrorMessage result = testClient.post()
                .uri("/api/boxboxd/like/driver/XXX")
                .header("Authorization", "Bearer " + jwtTokenMock.createMockToken().getToken().getTokenValue())
                .exchange()
                .expectStatus().isNotFound()
                .expectBody(ErrorMessage.class)
                .returnResult().getResponseBody();

        assertThat(result).isNotNull();
        assertThat(result.getMessage()).isEqualTo("Entidade não existe na base de dados: XXX");
        assertThat(result.getStatus()).isEqualTo(404);

        testClient.post()
                .uri("/api/boxboxd/like/team/XXX")
                .header("Authorization", "Bearer " + jwtTokenMock.createMockToken().getToken().getTokenValue())
                .exchange()
                .expectStatus().isNotFound();

        testClient.post()
                .uri("/api/boxboxd/like/circuit/XXX")
                .header("Authorization", "Bearer " + jwtTokenMock.createMockToken().getToken().getTokenValue())
                .exchange()
                .expectStatus().isNotFound();
    }

    @Test
    @DisplayName("Add like to entity Type that does not exist")
    void addLikeToEntityTypeThatDoesNotExist() {
        ErrorMessage result = testClient.post()
                .uri("/api/boxboxd/like/XXX/VER")
                .header("Authorization", "Bearer " + jwtTokenMock.createMockToken().getToken().getTokenValue())
                .exchange()
                .expectStatus().isEqualTo(500)
                .expectBody(ErrorMessage.class)
                .returnResult().getResponseBody();

        assertThat(result).isNotNull();
        assertThat(result.getStatus()).isEqualTo(500);
        assertThat(result.getStatusText()).isEqualTo("Internal Server Error");
    }

    @Test
    @DisplayName("Add like to entity that already has a like")
    void addLikeToEntityThatAlreadyHasALike() {
        ErrorMessage result = testClient.post()
                .uri("/api/boxboxd/like/driver/HAM")
                .header("Authorization", "Bearer " + jwtTokenMock.createMockToken().getToken().getTokenValue())
                .exchange()
                .expectStatus().isEqualTo(409)
                .expectBody(ErrorMessage.class)
                .returnResult().getResponseBody();

        assertThat(result).isNotNull();
        assertThat(result.getMessage()).isEqualTo("Like ja existe para esse usuario e essa entidade de tipo: DRIVER");
        assertThat(result.getStatus()).isEqualTo(409);
        assertThat(result.getStatusText()).isEqualTo("Conflict");

        testClient.post()
                .uri("/api/boxboxd/like/team/Mercedes")
                .header("Authorization", "Bearer " + jwtTokenMock.createMockToken().getToken().getTokenValue())
                .exchange()
                .expectStatus().isEqualTo(409);

        testClient.post()
                .uri("/api/boxboxd/like/circuit/monaco")
                .header("Authorization", "Bearer " + jwtTokenMock.createMockToken().getToken().getTokenValue())
                .exchange()
                .expectStatus().isEqualTo(409);
    }

    @Test
    @DisplayName("Add like to entity without token (UNAUTHORIZED)")
    void addLikeToEntityWithoutToken() {
        testClient.post()
                .uri("/api/boxboxd/like/driver/VER")
                .exchange()
                .expectStatus().isUnauthorized();

        testClient.post()
                .uri("/api/boxboxd/like/team/Ferrari")
                .exchange()
                .expectStatus().isUnauthorized();

        testClient.post()
                .uri("/api/boxboxd/like/circuit/silverstone")
                .exchange()
                .expectStatus().isUnauthorized();
    }

    @Test
    @DisplayName("Remove like sucessfully")
    void removeLike() {
        testClient.delete()
                .uri("/api/boxboxd/like/driver/HAM")
                .header("Authorization", "Bearer " + jwtTokenMock.createMockToken().getToken().getTokenValue())
                .exchange()
                .expectStatus().isNoContent();

        testClient.delete()
                .uri("/api/boxboxd/like/team/Mercedes")
                .header("Authorization", "Bearer " + jwtTokenMock.createMockToken().getToken().getTokenValue())
                .exchange()
                .expectStatus().isNoContent();

        testClient.delete()
                .uri("/api/boxboxd/like/circuit/monaco")
                .header("Authorization", "Bearer " + jwtTokenMock.createMockToken().getToken().getTokenValue())
                .exchange()
                .expectStatus().isNoContent();
    }

    @Test
    @DisplayName("Remove like that does not exist")
    void removeLikeThatDoesNotExist() {
        ErrorMessage result = testClient.delete()
                .uri("/api/boxboxd/like/driver/VER")
                .header("Authorization", "Bearer " + jwtTokenMock.createMockToken().getToken().getTokenValue())
                .exchange()
                .expectStatus().isEqualTo(404)
                .expectBody(ErrorMessage.class)
                .returnResult().getResponseBody();

        assertThat(result).isNotNull();
        assertThat(result.getMessage()).isEqualTo("Like não existe para esse usuario e essa entidade de tipo: DRIVER");
        assertThat(result.getStatus()).isEqualTo(404);
        assertThat(result.getStatusText()).isEqualTo("Not Found");

        testClient.delete()
                .uri("/api/boxboxd/like/team/Ferrari")
                .header("Authorization", "Bearer " + jwtTokenMock.createMockToken().getToken().getTokenValue())
                .exchange()
                .expectStatus().isEqualTo(404);

        testClient.delete()
                .uri("/api/boxboxd/like/circuit/silverstone")
                .header("Authorization", "Bearer " + jwtTokenMock.createMockToken().getToken().getTokenValue())
                .exchange()
                .expectStatus().isEqualTo(404);
    }

    @Test
    @DisplayName("Remove like without token (UNAUTHORIZED)")
    void removeLikeWithoutToken() {
        testClient.delete()
                .uri("/api/boxboxd/like/driver/HAM")
                .exchange()
                .expectStatus().isUnauthorized();

        testClient.delete()
                .uri("/api/boxboxd/like/team/Mercedes")
                .exchange()
                .expectStatus().isUnauthorized();

        testClient.delete()
                .uri("/api/boxboxd/like/circuit/monaco")
                .exchange()
                .expectStatus().isUnauthorized();
    }

    @Test
    @DisplayName("Remove like to entity Type that does not exist")
    void removeLikeToEntityTypeThatDoesNotExist() {
        ErrorMessage result = testClient.delete()
                .uri("/api/boxboxd/like/XXX/VER")
                .header("Authorization", "Bearer " + jwtTokenMock.createMockToken().getToken().getTokenValue())
                .exchange()
                .expectStatus().isEqualTo(500)
                .expectBody(ErrorMessage.class)
                .returnResult().getResponseBody();

        assertThat(result).isNotNull();
        assertThat(result.getStatus()).isEqualTo(500);
        assertThat(result.getStatusText()).isEqualTo("Internal Server Error");
    }
}
