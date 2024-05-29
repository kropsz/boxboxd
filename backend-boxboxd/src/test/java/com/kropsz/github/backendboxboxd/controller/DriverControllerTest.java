package com.kropsz.github.backendboxboxd.controller;

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

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@Sql(scripts = "/database/drivers/drivers.sql", executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
@Sql(scripts = "/database/drivers/delete-drivers.sql", executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
@Import(TestConfig.class)
class DriverControllerTest {

    @Autowired
    WebTestClient testClient;

    @Autowired
    JwtTokenMock jwtTokenMock;

    @Test
    @DisplayName("Get drivers by propertys")
    void testGetDriversByProperty() throws JsonProcessingException {

        JwtAuthenticationToken token = jwtTokenMock.createMockToken();
        String result = testClient.get()
                .uri("/api/boxboxd/drivers?property=code&value=HAM")
                .header("Authorization", "Bearer " + token.getToken().getTokenValue())
                .exchange()
                .expectStatus().isOk()
                .expectBody(String.class)
                .returnResult().getResponseBody();

        ObjectMapper mapper = new ObjectMapper();
        JsonNode root = mapper.readTree(result);
        int totalElements = root.path("totalElements").asInt();
        String code = root.path("content").get(0).path("code").asText();
        String name = root.path("content").get(0).path("name").asText();

        assertEquals(1, totalElements);
        assertEquals("HAM", code);
        assertEquals("Lewis", name);
    }

    @Test
    @DisplayName("Get drivers by propertys unauthenticated")
    void testGetDriversByPropertyUnauthenticated() {

        testClient.get()
                .uri("/api/boxboxd/drivers?property=code&value=HAM")
                .exchange()
                .expectStatus().isUnauthorized();
    }

    @Test
    @DisplayName("Get drivers by propertys not found")
    void testGetDriversByPropertyNotFound() throws JsonProcessingException {

        JwtAuthenticationToken token = jwtTokenMock.createMockToken();
        String result = testClient.get()
                .uri("/api/boxboxd/drivers?property=code&value=XXX")
                .header("Authorization", "Bearer " + token.getToken().getTokenValue())
                .exchange()
                .expectStatus().isOk()
                .expectBody(String.class)
                .returnResult().getResponseBody();

        ObjectMapper mapper = new ObjectMapper();
        JsonNode root = mapper.readTree(result);
        int totalElements = root.path("totalElements").asInt();

        assertEquals(0, totalElements);
    }

    @Test
    @DisplayName("Get drivers by propertys invalid property")
    void testGetDriversByPropertyInvalidProperty() {

        JwtAuthenticationToken token = jwtTokenMock.createMockToken();
        testClient.get()
                .uri("/api/boxboxd/drivers?property=xxx&value=HAM")
                .header("Authorization", "Bearer " + token.getToken().getTokenValue())
                .exchange()
                .expectStatus().isEqualTo(500);
    }

    @Test
    @DisplayName("Get drivers by propertys order by null")
    void testGetDriversByPropertyOrderByNull() throws JsonProcessingException {

        JwtAuthenticationToken token = jwtTokenMock.createMockToken();
        String result = testClient.get()
                .uri("/api/boxboxd/drivers?property=code&value=HAM&orderBy=")
                .header("Authorization", "Bearer " + token.getToken().getTokenValue())
                .exchange()
                .expectStatus().isOk()
                .expectBody(String.class)
                .returnResult().getResponseBody();

        ObjectMapper mapper = new ObjectMapper();
        JsonNode root = mapper.readTree(result);
        String code = root.path("content").get(0).path("code").asText();
        String name = root.path("content").get(0).path("name").asText();

        assertEquals("HAM", code);
        assertEquals("Lewis", name);
    }

}
