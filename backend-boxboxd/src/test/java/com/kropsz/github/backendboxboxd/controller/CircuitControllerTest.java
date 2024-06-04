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
@Sql(scripts = "/database/circuits/circuits.sql", executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
@Sql(scripts = "/database/circuits/delete-circuits.sql", executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
@Import(TestConfig.class)
class CircuitControllerTest {
    
    @Autowired
    WebTestClient testClient;

    @Autowired
    JwtTokenMock jwtTokenMock;

    @Test
    @DisplayName("Get circuits by propertys")
    void testGetCircuitsByProperty() throws JsonProcessingException {

        JwtAuthenticationToken token = jwtTokenMock.createMockToken();
        String result = testClient.get()
                .uri("/api/boxboxd/circuits?property=name&value=Circuit de Monaco")
                .header("Authorization", "Bearer " + token.getToken().getTokenValue())
                .exchange()
                .expectStatus().isOk()
                .expectBody(String.class)
                .returnResult().getResponseBody();

        ObjectMapper mapper = new ObjectMapper();
        JsonNode root = mapper.readTree(result);
        int totalElements = root.path("totalElements").asInt();
        String name = root.path("content").get(0).path("name").asText();
        String description = root.path("content").get(0).path("description").asText();

        assertEquals(1, totalElements);
        assertEquals("Circuit de Monaco", name);
        assertEquals("Street circuit in Monte Carlo", description);
    }

    @Test
    @DisplayName("Get circuits by propertys unauthenticated")
    void testGetCircuitsByPropertyUnauthenticated() {
        testClient.get()
                .uri("/api/boxboxd/circuits?property=name&value=Circuit de Monaco")
                .exchange()
                .expectStatus().isUnauthorized();
    }

    @Test
    @DisplayName("Get circuits by order atribute don't exists")
    void testGetCircuitsByOrderAttributeDontExists() {

        JwtAuthenticationToken token = jwtTokenMock.createMockToken();
        ErrorMessage result = testClient.get()
                .uri("/api/boxboxd/circuits?orderBy=XXX")
                .header("Authorization", "Bearer " + token.getToken().getTokenValue())
                .exchange()
                .expectStatus().isEqualTo(500)
                .expectBody(ErrorMessage.class)
                .returnResult().getResponseBody();

        assertThat(result).isNotNull();
        assertThat(result.getMessage()).isEqualTo("No property 'XXX' found for type 'Circuits'");
        assertThat(result.getStatus()).isEqualTo(500);
    }

    @Test
    @DisplayName("Get circuits by propertys atribute don't exists")
    void testGetCircuitsByPropertyAttributeDontExists() {

        JwtAuthenticationToken token = jwtTokenMock.createMockToken();
        ErrorMessage result = testClient.get()
                .uri("/api/boxboxd/circuits?property=XXX&value=Circuit de Monaco")
                .header("Authorization", "Bearer " + token.getToken().getTokenValue())
                .exchange()
                .expectStatus().isEqualTo(500)
                .expectBody(ErrorMessage.class)
                .returnResult().getResponseBody();

        assertThat(result).isNotNull();
        assertThat(result.getMessage()).isEqualTo("Could not resolve attribute 'XXX' of 'com.kropsz.github.backendboxboxd.entities.Circuits'");
        assertThat(result.getStatus()).isEqualTo(500);
    }

    @Test
    @DisplayName("Get circuits by propertys order by null")
    void testGetCircuitsByPropertyOrderByNull() throws JsonProcessingException {

        JwtAuthenticationToken token = jwtTokenMock.createMockToken();
        String result = testClient.get()
                .uri("/api/boxboxd/circuits?property=name&value=Circuit de Monaco&orderBy=")
                .header("Authorization", "Bearer " + token.getToken().getTokenValue())
                .exchange()
                .expectStatus().isOk()
                .expectBody(String.class)
                .returnResult().getResponseBody();

                ObjectMapper mapper = new ObjectMapper();
                JsonNode root = mapper.readTree(result);
                String name = root.path("content").get(0).path("name").asText();
        

        assertEquals("Circuit de Monaco", name);
        
    }

}
