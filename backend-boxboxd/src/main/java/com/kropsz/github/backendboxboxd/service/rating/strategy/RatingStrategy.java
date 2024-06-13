package com.kropsz.github.backendboxboxd.service.rating.strategy;

public interface RatingStrategy {
    void updateRating(String entityId, Double rating);

    void removeRating(String entityId, Double rating);

    void changeRating(String entityId, Double oldRating, Double newRating);
}
