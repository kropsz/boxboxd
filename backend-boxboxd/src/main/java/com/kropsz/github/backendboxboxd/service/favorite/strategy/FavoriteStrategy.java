package com.kropsz.github.backendboxboxd.service.favorite.strategy;

public interface FavoriteStrategy {
    
    boolean exists(String id, Long userId);
    boolean execute(String id, Long userId);
    boolean supports(String type);

}
