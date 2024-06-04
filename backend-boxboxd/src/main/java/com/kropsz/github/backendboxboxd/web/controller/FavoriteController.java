package com.kropsz.github.backendboxboxd.web.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationToken;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.kropsz.github.backendboxboxd.service.favorite.FavoriteDriverService;
import com.kropsz.github.backendboxboxd.service.favorite.FavoriteTeamSerivce;

import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/boxboxd")
public class FavoriteController {
    
    private final FavoriteDriverService favoriteDriverService;
    private final FavoriteTeamSerivce  favoriteTeamSerivce;


    @PostMapping("/favorite/driver/{driverCode}")
    public ResponseEntity<Void> addFavoriteDriver(@PathVariable String driverCode, JwtAuthenticationToken token) {
        var userId = Long.parseLong(token.getName());
        favoriteDriverService.addFavorite(driverCode, userId);
        return ResponseEntity.ok().build();
    }

    @DeleteMapping("/favorite/driver/{driverCode}")
    public ResponseEntity<Void> removeFavoriteDriver(@PathVariable String driverCode, JwtAuthenticationToken token) {
        var userId = Long.parseLong(token.getName());
        favoriteDriverService.removeFavorite(driverCode, userId);
        return ResponseEntity.noContent().build();
    }

    @PostMapping("/favorite/team/{teamName}")
    public ResponseEntity<Void> addFavoriteTeam(@PathVariable String teamName, JwtAuthenticationToken token) {
        var userId = Long.parseLong(token.getName());
        favoriteTeamSerivce.addFavorite(teamName, userId);
        return ResponseEntity.ok().build();
    }

    @DeleteMapping("/favorite/team/{teamName}")
    public ResponseEntity<Void> removeFavoriteTeam(@PathVariable String teamName, JwtAuthenticationToken token) {
        var userId = Long.parseLong(token.getName());
        favoriteTeamSerivce.removeFavorite(teamName, userId);
        return ResponseEntity.noContent().build();
    }

}
