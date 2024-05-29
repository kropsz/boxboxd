package com.kropsz.github.backendboxboxd.controller;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationToken;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.web.reactive.server.WebTestClient;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.kropsz.github.backendboxboxd.security.JwtTokenMock;
import com.kropsz.github.backendboxboxd.security.TestConfig;
import com.kropsz.github.backendboxboxd.web.exceptions.ErrorMessage;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@Sql(scripts = "/database/teams/teams.sql", executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
@Sql(scripts = "/database/teams/delete-teams.sql", executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
@Import(TestConfig.class)
class TeamControllerTest {

    @Autowired
    WebTestClient testClient;

    @Autowired
    JwtTokenMock jwtTokenMock;

    @Test
    @DisplayName("Get teams by propertys")
    void testGetTeamsByProperty() throws JsonProcessingException {

        JwtAuthenticationToken token = jwtTokenMock.createMockToken();
        String result = testClient.get()
                .uri("/api/boxboxd/teams?property=name&value=Ferrari")
                .header("Authorization", "Bearer " + token.getToken().getTokenValue())
                .exchange()
                .expectStatus().isOk()
                .expectBody(String.class)
                .returnResult().getResponseBody();

        ObjectMapper mapper = new ObjectMapper();
        JsonNode root = mapper.readTree(result);
        int totalElements = root.path("totalElements").asInt();
        String name = root.path("content").get(0).path("name").asText();
        String country = root.path("content").get(0).path("country").asText();

        assertEquals(1, totalElements);
        assertEquals("Ferrari", name);
        assertEquals("Italy", country);
    }

    @Test
    @DisplayName("Get teams by propertys unauthenticated")
    void testGetTeamsByPropertyUnauthenticated() {

        testClient.get()
                .uri("/api/boxboxd/teams?property=name&value=Ferrari")
                .exchange()
                .expectStatus().isUnauthorized();
    }

    @Test
    @DisplayName("Get teams by order atribute don't exists")
    void testGetTeamsByPropertyNotFound() throws JsonProcessingException {

        JwtAuthenticationToken token = jwtTokenMock.createMockToken();
        ErrorMessage result = testClient.get()
                .uri("/api/boxboxd/teams?orderBy=xxx")
                .header("Authorization", "Bearer " + token.getToken().getTokenValue())
                .exchange()
                .expectStatus().isEqualTo(500)
                .expectBody(ErrorMessage.class)
                .returnResult().getResponseBody();

        assertThat(result).isNotNull();
        assertThat(result.getMessage()).isEqualTo("No property 'xxx' found for type 'Team'");
        assertThat(result.getStatus()).isEqualTo(500);

    }

    @Test
    @DisplayName("Get teams by propertys atribute don't exists")
    void testGetTeamsByPropertyAtributNotFound() throws JsonProcessingException {

        JwtAuthenticationToken token = jwtTokenMock.createMockToken();
        ErrorMessage result = testClient.get()
                .uri("/api/boxboxd/teams?property=xxx&value=Ferrari")
                .header("Authorization", "Bearer " + token.getToken().getTokenValue())
                .exchange()
                .expectStatus().isEqualTo(500)
                .expectBody(ErrorMessage.class)
                .returnResult().getResponseBody();

        assertThat(result).isNotNull();
        assertThat(result.getMessage()).isEqualTo("Could not resolve attribute 'xxx' of 'com.kropsz.github.backendboxboxd.entities.Team'");
        assertThat(result.getStatus()).isEqualTo(500);

    }


    @Test
    @DisplayName("Get drivers by propertys order by null")
    void testGetTeamsByPropertyOrderByNull() throws JsonProcessingException {

        JwtAuthenticationToken token = jwtTokenMock.createMockToken();
        String result = testClient.get()
                .uri("/api/boxboxd/teams?property=name&value=Ferrari&orderBy=")
                .header("Authorization", "Bearer " + token.getToken().getTokenValue())
                .exchange()
                .expectStatus().isOk()
                .expectBody(String.class)
                .returnResult().getResponseBody();

        ObjectMapper mapper = new ObjectMapper();
        JsonNode root = mapper.readTree(result);
        int totalElements = root.path("totalElements").asInt();
        String name = root.path("content").get(0).path("name").asText();
        String country = root.path("content").get(0).path("country").asText();

        assertEquals(1, totalElements);
        assertEquals("Ferrari", name);
        assertEquals("Italy", country);
    }

}
