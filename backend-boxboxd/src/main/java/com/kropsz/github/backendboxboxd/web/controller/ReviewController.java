package com.kropsz.github.backendboxboxd.web.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationToken;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.kropsz.github.backendboxboxd.entities.like.EntityType;
import com.kropsz.github.backendboxboxd.service.review.ReviewService;
import com.kropsz.github.backendboxboxd.web.dtos.ReviewDto;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/boxboxd")
@RequiredArgsConstructor
public class ReviewController {

    private final ReviewService reviewService;

    @PostMapping("/review/{type}/{entityId}")
    public ResponseEntity<Void> review(@PathVariable String type,
            @PathVariable String entityId,
            @RequestBody ReviewDto reviewDto,
            JwtAuthenticationToken token) throws JsonProcessingException {
                        var enumType = EntityType.valueOf(type.toUpperCase());

        var userId = Long.parseLong(token.getName());
        reviewService.publisherReview(userId, enumType, entityId, reviewDto);
        return ResponseEntity.ok().build();
    }

}
