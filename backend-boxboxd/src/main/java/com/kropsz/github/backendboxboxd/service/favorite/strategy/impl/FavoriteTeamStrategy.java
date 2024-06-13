package com.kropsz.github.backendboxboxd.service.favorite.strategy.impl;

import java.util.Optional;

import org.springframework.stereotype.Component;

import com.kropsz.github.backendboxboxd.entities.favorites.FavoriteTeam;
import com.kropsz.github.backendboxboxd.repository.favorite.FavoriteTeamRepository;
import com.kropsz.github.backendboxboxd.service.favorite.strategy.FavoriteStrategy;

import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class FavoriteTeamStrategy implements FavoriteStrategy{
    
    private final FavoriteTeamRepository favoriteTeamRepository;
    
    @Override
    public boolean execute(String name, Long userId) {
        Optional<FavoriteTeam> existingFavoriteTeam = favoriteTeamRepository.findByTeamNameAndUserId(name, userId);
        return !existingFavoriteTeam.isPresent();    
    }

    @Override
    public boolean exists(String name, Long userId) {
        Optional<FavoriteTeam> existingFavoriteTeam = favoriteTeamRepository.findByTeamNameAndUserId(name, userId);
        return existingFavoriteTeam.isPresent();    
    }
    
    @Override
    public boolean supports(String type) {
        return type.equals("team");
    }
}
