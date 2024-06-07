package com.kropsz.github.backendboxboxd.service.like.strategy.impl;

import org.springframework.stereotype.Component;

import com.kropsz.github.backendboxboxd.entities.Driver;
import com.kropsz.github.backendboxboxd.exception.NotFoundException;
import com.kropsz.github.backendboxboxd.repository.DriverRepository;
import com.kropsz.github.backendboxboxd.service.like.strategy.EntityStrategy;

import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class DriverStrategy implements EntityStrategy {
    private final DriverRepository driverRepository;

    @Override
    public boolean existsById(String entityId) {
        return driverRepository.existsById(entityId);
    }

    @Override
    public Object findById(String entityId) {
        return driverRepository.findById(entityId)
                .orElseThrow(() -> new NotFoundException("Piloto não encontrado"));
    }

    @Override
    public void save(Object entity) {
        driverRepository.save((Driver) entity);
    }

    @Override
    public void incrementLikeCounter(Object entity) {
        ((Driver) entity).setLikes(((Driver) entity).getLikes() + 1);
    }

    @Override
    public void decrementLikeCounter(Object entity) {
        ((Driver) entity).setLikes(((Driver) entity).getLikes() - 1);
    }
}