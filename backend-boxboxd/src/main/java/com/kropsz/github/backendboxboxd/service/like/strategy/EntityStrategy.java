package com.kropsz.github.backendboxboxd.service.like.strategy;

public interface EntityStrategy {
    boolean existsById(String entityId);
    Object findById(String entityId);
    void save(Object entity);
    void incrementLikeCounter(Object entity);
    void decrementLikeCounter(Object entity);
}
