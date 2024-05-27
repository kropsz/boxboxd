package com.kropsz.github.backendboxboxd.web.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationToken;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.kropsz.github.backendboxboxd.service.FavoriteDriverService;

import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/boxboxd")
public class FavoriteController {
    
    private final FavoriteDriverService favoriteService;

    @PostMapping("/favorite/driver/{driverCode}")
    public ResponseEntity<Void> addFavoriteDriver(@PathVariable String driverCode, JwtAuthenticationToken token) {
        var userId = Long.parseLong(token.getName());
        favoriteService.addFavoriteDriver(driverCode, userId);
        return ResponseEntity.ok().build();
    }

    @DeleteMapping("/favorite/driver/{driverCode}")
    public ResponseEntity<Void> removeFavoriteDriver(@PathVariable String driverCode, JwtAuthenticationToken token) {
        var userId = Long.parseLong(token.getName());
        favoriteService.removeFavoriteDriver(driverCode, userId);
        return ResponseEntity.ok().build();
    }
}
