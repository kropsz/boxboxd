package com.kropsz.github.backendboxboxd.web.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationToken;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.kropsz.github.backendboxboxd.entities.like.EntityType;
import com.kropsz.github.backendboxboxd.service.like.LikeService;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("api/boxboxd/")
@RequiredArgsConstructor
public class LikeController {

    private final LikeService likeService;

    @PostMapping("/like/{entityType}/{entityId}")
    public ResponseEntity<Void> addLike(@PathVariable String entityId, @PathVariable String entityType,
            JwtAuthenticationToken token) {
        var typeUpperCase = EntityType.valueOf(entityType.toUpperCase());
        var userId = Long.parseLong(token.getName());
        likeService.addLike(userId, entityId, typeUpperCase);
        return ResponseEntity.ok().build();
    }

    @DeleteMapping("/like/{entityType}/{entityId}")
    public ResponseEntity<Void> removeLike(@PathVariable String entityId, @PathVariable EntityType entityType,
            JwtAuthenticationToken token) {
        var userId = Long.parseLong(token.getName());
        likeService.removeLike(userId, entityId, entityType);
        return ResponseEntity.noContent().build();
    }
}