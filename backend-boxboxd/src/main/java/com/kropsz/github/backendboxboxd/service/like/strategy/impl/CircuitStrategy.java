package com.kropsz.github.backendboxboxd.service.like.strategy.impl;

import org.springframework.stereotype.Component;

import com.kropsz.github.backendboxboxd.entities.Circuits;
import com.kropsz.github.backendboxboxd.exception.NotFoundException;
import com.kropsz.github.backendboxboxd.repository.CircuitsRepository;
import com.kropsz.github.backendboxboxd.service.like.strategy.EntityStrategy;

import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class CircuitStrategy implements EntityStrategy{
    
    private final CircuitsRepository circuitRepository;

    @Override
    public boolean existsById(String entityId) {
        return circuitRepository.existsById(entityId);
    }

    @Override
    public Object findById(String entityId) {
        return circuitRepository.findById(entityId)
                .orElseThrow(() -> new NotFoundException("Circuito não encontrado"));
    }

    @Override
    public void save(Object entity) {
        circuitRepository.save((Circuits) entity);
    }

    @Override
    public void incrementLikeCounter(Object entity) {
        ((Circuits) entity).setLikes(((Circuits) entity).getLikes() + 1);
    }

    @Override
    public void decrementLikeCounter(Object entity) {
        ((Circuits) entity).setLikes(((Circuits) entity).getLikes() - 1);
    }
}
