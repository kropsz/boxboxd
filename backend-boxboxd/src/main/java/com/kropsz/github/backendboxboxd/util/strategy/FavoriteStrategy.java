package com.kropsz.github.backendboxboxd.util.strategy;

public interface FavoriteStrategy {
    
    boolean execute(String id, Long userId);
    boolean supports(String type);

}
