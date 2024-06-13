package com.kropsz.github.backendboxboxd.service.favorite.factory.impl;

import org.springframework.stereotype.Component;

import com.kropsz.github.backendboxboxd.entities.favorites.FavoriteDriver;
import com.kropsz.github.backendboxboxd.service.favorite.factory.ObjectFactory;

@Component

public class FavoriteDriverFactory implements ObjectFactory{

    @Override
    public FavoriteDriver create(String code, Long userId) {
        FavoriteDriver favoriteDriver = new FavoriteDriver();
        favoriteDriver.setDriverCode(code);
        favoriteDriver.setUserId(userId);
        return favoriteDriver;
    }
    
}
