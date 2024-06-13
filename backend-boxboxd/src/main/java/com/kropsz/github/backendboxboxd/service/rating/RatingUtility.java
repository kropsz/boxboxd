package com.kropsz.github.backendboxboxd.service.rating;

import java.util.EnumMap;

import org.springframework.stereotype.Component;

import com.kropsz.github.backendboxboxd.entities.like.EntityType;
import com.kropsz.github.backendboxboxd.entities.rating.Rate;
import com.kropsz.github.backendboxboxd.service.rating.strategy.CircuitRatingStrategy;
import com.kropsz.github.backendboxboxd.service.rating.strategy.DriverRatingStrategy;
import com.kropsz.github.backendboxboxd.service.rating.strategy.RatingStrategy;

@Component
public class RatingUtility {

    private EnumMap<EntityType, RatingStrategy> strategies;

    public RatingUtility(DriverRatingStrategy driverStrategy, CircuitRatingStrategy circuitStrategy) {
        this.strategies = new EnumMap<>(EntityType.class);
        strategies.put(EntityType.DRIVER, driverStrategy);
        strategies.put(EntityType.CIRCUIT, circuitStrategy);
    }

    public Rate createRating(Long userId, String entityId, Double rating, EntityType type) {
        Rate ratingEntity = new Rate();
        ratingEntity.setUserId(userId);
        ratingEntity.setEntityId(entityId);
        ratingEntity.setRating(rating);
        ratingEntity.setType(type);
        return ratingEntity;
    }

    public void updateRating(String entityId, Double rating, EntityType type) {
        strategies.get(type).updateRating(entityId, rating);
    }

    public void deleteRating(String entity, Double rating, EntityType type) {
        strategies.get(type).removeRating(entity, rating);
    }

    public void changeRating(String entityId, Double newRating, EntityType type, Double oldRating) {
        strategies.get(type).changeRating(entityId, oldRating, newRating);
    }
}
