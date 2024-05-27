package com.kropsz.github.backendboxboxd.util.strategy.impl;

import java.util.Optional;

import org.springframework.stereotype.Component;

import com.kropsz.github.backendboxboxd.entities.favorites.FavoriteDriver;
import com.kropsz.github.backendboxboxd.repository.favorite.FavoriteDriverRepository;
import com.kropsz.github.backendboxboxd.util.strategy.FavoriteStrategy;

import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class FavoriteDriverStrategy implements FavoriteStrategy {
    
    private final FavoriteDriverRepository favoriteDriverRepository;

        @Override
    public boolean execute(String code, Long userId) {
        Optional<FavoriteDriver> existingFavoriteDriver = favoriteDriverRepository.findByDriverCodeAndUserId(code, userId);
        return !existingFavoriteDriver.isPresent();        
    }
    
    @Override
    public boolean supports(String type) {
        return type.equals("driver");
    }
}
