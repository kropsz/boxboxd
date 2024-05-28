package com.kropsz.github.backendboxboxd.util.factory.impl;

import org.springframework.stereotype.Component;

import com.kropsz.github.backendboxboxd.entities.favorites.FavoriteTeam;
import com.kropsz.github.backendboxboxd.util.factory.ObjectFactory;

@Component
public class FavoriteTeamFactory implements ObjectFactory{

    @Override
    public FavoriteTeam create(String id, Long userId) {
        FavoriteTeam favoriteTeam = new FavoriteTeam();
        favoriteTeam.setTeamName(id);
        favoriteTeam.setUserId(userId);
        return favoriteTeam;
    }
    
}
