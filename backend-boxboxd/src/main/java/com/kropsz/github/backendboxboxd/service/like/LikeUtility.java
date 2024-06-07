package com.kropsz.github.backendboxboxd.service.like;

import java.util.EnumMap;

import org.springframework.stereotype.Component;

import com.kropsz.github.backendboxboxd.entities.like.EntityType;
import com.kropsz.github.backendboxboxd.exception.TypeNotFoundException;
import com.kropsz.github.backendboxboxd.repository.like.LikeRepository;
import com.kropsz.github.backendboxboxd.service.like.strategy.EntityStrategy;
import com.kropsz.github.backendboxboxd.service.like.strategy.impl.CircuitStrategy;
import com.kropsz.github.backendboxboxd.service.like.strategy.impl.DriverStrategy;
import com.kropsz.github.backendboxboxd.service.like.strategy.impl.TeamStrategy;

@Component
public class LikeUtility {

    private EnumMap<EntityType, EntityStrategy> strategyMap;
    private LikeRepository likeRepository;


    public LikeUtility(DriverStrategy driverStrategy, TeamStrategy teamStrategy,
            CircuitStrategy circuitsStrategy, LikeRepository likeRepository) {
        this.likeRepository = likeRepository;
        strategyMap = new EnumMap<>(EntityType.class);
        strategyMap.put(EntityType.DRIVER, driverStrategy);
        strategyMap.put(EntityType.TEAM, teamStrategy);
        strategyMap.put(EntityType.CIRCUIT, circuitsStrategy);
    }

    public boolean isLikeAlreadyExists(Long userId, String entityId) {
        return likeRepository.existsByUserIdAndEntityId(userId, entityId);
    }

    public boolean isEntityExists(String entityId, EntityType entityType) {
        return getStrategy(entityType).existsById(entityId);
    }

    public void incrementLikeCounter(String entityId, EntityType entityType) {
        EntityStrategy strategy = getStrategy(entityType);
        Object entity = strategy.findById(entityId);
        strategy.incrementLikeCounter(entity);
        strategy.save(entity);
    }

    public void decrementLikeCounter(String entityId, EntityType entityType) {
        EntityStrategy strategy = getStrategy(entityType);
        Object entity = strategy.findById(entityId);
        strategy.decrementLikeCounter(entity);
        strategy.save(entity);
    }

    private EntityStrategy getStrategy(EntityType entityType) {
        EntityStrategy strategy = strategyMap.get(entityType);
        if (strategy == null) {
            throw new TypeNotFoundException("Tipo não encontrado: " + entityType);
        }
        return strategy;
    }
}
