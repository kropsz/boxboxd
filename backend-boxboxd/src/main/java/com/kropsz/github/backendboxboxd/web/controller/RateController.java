package com.kropsz.github.backendboxboxd.web.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationToken;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.kropsz.github.backendboxboxd.entities.like.EntityType;
import com.kropsz.github.backendboxboxd.service.rating.RatingService;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("api/boxboxd/")
@RequiredArgsConstructor
public class RateController {

    private final RatingService ratingService;

    @PostMapping("/rate/{type}/{entityId}/{rating}")
    public ResponseEntity<Void> rate(@PathVariable String type,
            @PathVariable String entityId,
            @PathVariable Double rating,
            JwtAuthenticationToken token) {

        var userId = Long.parseLong(token.getName());
        var enumType = EntityType.valueOf(type.toUpperCase());
        ratingService.saveRating(userId, entityId, rating, enumType);
        return ResponseEntity.ok().build();
    }

    @DeleteMapping("/rate/{type}/{entityId}")
    public ResponseEntity<Void> deleteRate(@PathVariable String type,
            @PathVariable String entityId,
            JwtAuthenticationToken token) {

        var userId = Long.parseLong(token.getName());
        var enumType = EntityType.valueOf(type.toUpperCase());
        ratingService.deleteRating(userId, entityId, enumType);
        return ResponseEntity.ok().build();
    }

    @PutMapping("/rate/{type}/{entityId}/{newRating}")
    public ResponseEntity<Void> updateRate(@PathVariable String type,
            @PathVariable String entityId,
            @PathVariable Double newRating,
            JwtAuthenticationToken token) {

        var userId = Long.parseLong(token.getName());
        var enumType = EntityType.valueOf(type.toUpperCase());
        ratingService.updateRating(userId, entityId, newRating, enumType);
        return ResponseEntity.ok().build();
    }
}
