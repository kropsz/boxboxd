package com.kropsz.github.backendboxboxd.controller;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationToken;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.web.reactive.server.WebTestClient;

import com.kropsz.github.backendboxboxd.security.JwtTokenMock;
import com.kropsz.github.backendboxboxd.security.TestConfig;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@Sql(scripts = "/database/drivers/drivers.sql", executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
@Sql(scripts = "/database/drivers/delete-drivers.sql", executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
@Sql(scripts = "/database/teams/teams.sql", executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
@Sql(scripts = "/database/teams/delete-teams.sql", executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
@Import(TestConfig.class)
class FavoriteControllerTest {

    @Autowired
    WebTestClient testClient;

    @Autowired
    JwtTokenMock jwtTokenMock;

    @Test
    @DisplayName("Add favorite driver successullfuly")
    void testAddFavoriteDriver() {
        String driverCode = "HAM";

        JwtAuthenticationToken token = jwtTokenMock.createMockToken();
        testClient.post()
                .uri("/api/boxboxd/favorite/driver/" + driverCode)
                .header("Authorization", "Bearer " + token.getToken().getTokenValue())
                .exchange()
                .expectStatus().isOk();
    }

    @Test
    @DisplayName("Add favorite driver not found")
    void testAddFavoriteDriverNotFound() {
        String driverCode = "FAIL";

        JwtAuthenticationToken token = jwtTokenMock.createMockToken();
        testClient.post()
                .uri("/api/boxboxd/favorite/driver/" + driverCode)
                .header("Authorization", "Bearer " + token.getToken().getTokenValue())
                .exchange()
                .expectStatus().isNotFound();
    }

    @Test
    @DisplayName("Add favorite driver conflict")
    void testAddFavoriteDriverConflict() {
        String driverCode = "VER";

        JwtAuthenticationToken token = jwtTokenMock.createMockToken();

        testClient.post()
                .uri("/api/boxboxd/favorite/driver/" + driverCode)
                .header("Authorization", "Bearer " + token.getToken().getTokenValue())
                .exchange()
                .expectStatus().isEqualTo(409);
    }

    @Test
    @DisplayName("Add favorite driver unauthorized")
    void testAddFavoriteDriverUnauthorized() {
        String driverCode = "HAM";

        String token = "123";

        testClient.post()
                .uri("/api/boxboxd/favorite/driver/" + driverCode)
                .header("Authorization", "Bearer " + token)
                .exchange()
                .expectStatus().isUnauthorized();
    }

    @Test
    @DisplayName("Remove favorite driver successullfuly")
    void testRemoveFavoriteDriver() {
        String driverCode = "VER";

        JwtAuthenticationToken token = jwtTokenMock.createMockToken();
        testClient.delete()
                .uri("/api/boxboxd/favorite/driver/" + driverCode)
                .header("Authorization", "Bearer " + token.getToken().getTokenValue())
                .exchange()
                .expectStatus().isNoContent();
    }

    @Test
    @DisplayName("Remove favorite driver not found")
    void testRemoveFavoriteDriverNotFound() {
        String driverCode = "FAIL";

        JwtAuthenticationToken token = jwtTokenMock.createMockToken();
        testClient.delete()
                .uri("/api/boxboxd/favorite/driver/" + driverCode)
                .header("Authorization", "Bearer " + token.getToken().getTokenValue())
                .exchange()
                .expectStatus().isNotFound();
    }

    @Test
    @DisplayName("Remove favorite driver unauthorized")
    void testRemoveFavoriteDriverUnauthorized() {
        String driverCode = "VER";
        String token = "123";

        testClient.post()
                .uri("/api/boxboxd/favorite/driver/" + driverCode)
                .header("Authorization", "Bearer " + token)
                .exchange()
                .expectStatus().isUnauthorized();
        
    }

    @Test
    @DisplayName("Add favorite team successullfuly")
    void testAddFavoriteTeam() {
        String teamCode = "Mercedes";

        JwtAuthenticationToken token = jwtTokenMock.createMockToken();
        testClient.post()
                .uri("/api/boxboxd/favorite/team/" + teamCode)
                .header("Authorization", "Bearer " + token.getToken().getTokenValue())
                .exchange()
                .expectStatus().isOk();
    }

    @Test
    @DisplayName("Add favorite team not found")
    void testAddFavoriteTeamNotFound() {
        String teamCode = "FAIL";

        JwtAuthenticationToken token = jwtTokenMock.createMockToken();
        testClient.post()
                .uri("/api/boxboxd/favorite/team/" + teamCode)
                .header("Authorization", "Bearer " + token.getToken().getTokenValue())
                .exchange()
                .expectStatus().isNotFound();
    }

    @Test
    @DisplayName("Add favorite team conflict")
    void testAddFavoriteTeamConflict() {
        String teamCode = "Ferrari";

        JwtAuthenticationToken token = jwtTokenMock.createMockToken();

        testClient.post()
                .uri("/api/boxboxd/favorite/team/" + teamCode)
                .header("Authorization", "Bearer " + token.getToken().getTokenValue())
                .exchange()
                .expectStatus().isEqualTo(409);
    }

    @Test
    @DisplayName("Add favorite team unauthorized")
    void testAddFavoriteTeamUnauthorized() {
        String teamCode = "Mercedes";
        String token = "123";

        testClient.post()
                .uri("/api/boxboxd/favorite/driver/" + teamCode)
                .header("Authorization", "Bearer " + token)
                .exchange()
                .expectStatus().isUnauthorized();
    }

    @Test
    @DisplayName("Remove favorite team successullfuly")
    void testRemoveFavoriteTeam() {
        String teamCode = "Ferrari";

        JwtAuthenticationToken token = jwtTokenMock.createMockToken();
        testClient.delete()
                .uri("/api/boxboxd/favorite/team/" + teamCode)
                .header("Authorization", "Bearer " + token.getToken().getTokenValue())
                .exchange()
                .expectStatus().isNoContent();
    }

    @Test
    @DisplayName("Remove favorite team not found")
    void testRemoveFavoriteTeamNotFound() {
        String teamCode = "FAIL";

        JwtAuthenticationToken token = jwtTokenMock.createMockToken();
        testClient.delete()
                .uri("/api/boxboxd/favorite/team/" + teamCode)
                .header("Authorization", "Bearer " + token.getToken().getTokenValue())
                .exchange()
                .expectStatus().isNotFound();
    }

    @Test
    @DisplayName("Remove favorite team unauthorized")
    void testRemoveFavoriteTeamUnauthorized() {
        String teamCode = "Ferrari";
        String token = "123";
        testClient.post()
                .uri("/api/boxboxd/favorite/driver/" + teamCode)
                .header("Authorization", "Bearer " + token)
                .exchange()
                .expectStatus().isUnauthorized();
    }
}
