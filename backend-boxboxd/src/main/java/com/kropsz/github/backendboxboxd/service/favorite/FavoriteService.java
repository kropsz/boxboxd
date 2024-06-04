package com.kropsz.github.backendboxboxd.service.favorite;

public interface FavoriteService {

    void addFavorite(String entityId, Long userId);

    void removeFavorite(String entityId, Long userId);
}
